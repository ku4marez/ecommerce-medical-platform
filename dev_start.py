#!/usr/bin/env python3
import subprocess, os, sys, platform

SERVICES = ["catalog", "inventory", "order", "payment", "gateway"]
DOCKER_USER = "ku4marez"
REPO = "myrepo"
HELM_DIR = "infra/helm"

def run(cmd):
    print(f"\n▶️  {cmd}")
    subprocess.run(cmd, shell=True, check=True)

def main():
    print("Starting local k3d dev cluster setup")

    # 1. Create or reuse cluster
    try:
        subprocess.run("k3d cluster get dev-cluster", shell=True, check=True, stdout=subprocess.DEVNULL)
        print("k3d cluster already running")
    except subprocess.CalledProcessError:
        run("k3d cluster create dev-cluster --servers 1 --agents 2 --port 8080:80@loadbalancer")

    # 2. Build + push images
    for svc in SERVICES:
        svc_dir = os.path.join("services", f"{svc}-service")
        if not os.path.isdir(svc_dir):
            print(f"⚠️  Skipping {svc}, directory not found.")
            continue
        tag = f"{DOCKER_USER}/{REPO}:{svc}-latest"
        run(f"docker build -t {tag} {svc_dir}")
        run(f"docker push {tag}")

    # 3. Deploy with Helm
    for svc in SERVICES:
        chart_path = os.path.join(HELM_DIR, f"{svc}-service")
        run(f"helm upgrade --install {svc} {chart_path} "
            f"--set image.repository={DOCKER_USER}/{REPO} "
            f"--set image.tag={svc}-latest")

    # 4. Show cluster state
    run("kubectl get pods -A")
    run("kubectl get svc -A")

    print("\nDeployment complete. Visit http://localhost:8080 (via ingress).")

if __name__ == "__main__":
    try:
        main()
    except subprocess.CalledProcessError as e:
        print(f"Command failed: {e}")
        sys.exit(1)
