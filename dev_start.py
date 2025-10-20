#!/usr/bin/env python3
"""
Dev Cluster Startup Script
--------------------------
Bootstraps local Minikube cluster, builds all microservice Docker images,
and deploys them via Helm charts under infra/helm/.
"""

import subprocess
import os
import sys

SERVICES = ["catalog", "inventory", "order", "payment"]
HELM_DIR = "infra/helm"

def run(cmd, **kwargs):
    """Execute shell command."""
    print(f"\n▶️  {cmd}")
    subprocess.run(cmd, shell=True, check=True, **kwargs)

def main():
    print("🚀 Starting local development cluster setup")

    # Ensure Minikube is running
    try:
        subprocess.run("minikube status", shell=True, check=True, stdout=subprocess.DEVNULL)
        print("✅ Minikube already running")
    except subprocess.CalledProcessError:
        run("minikube start --driver=docker --cpus=4 --memory=8192")

    # Switch Docker context to Minikube
    print("🔧 Switching Docker context to Minikube")
    if os.name == "nt":  # Windows
        run('minikube docker-env | Invoke-Expression', executable="powershell.exe")
    else:
        run('eval $(minikube docker-env)')

    # Build Docker images for all services
    for svc in SERVICES:
        service_dir = os.path.join("services", f"{svc}-service")
        if not os.path.isdir(service_dir):
            print(f"⚠️  Skipping {svc}, directory not found.")
            continue
        print(f"🐳 Building {svc}-service Docker image")
        run(f"docker build -t {svc}-service:latest {service_dir}")

    # Deploy via Helm
    for svc in SERVICES:
        chart_path = os.path.join(HELM_DIR, f"{svc}-service")
        print(f"📦 Deploying {svc}-service via Helm")
        run(f"helm upgrade --install {svc} {chart_path}")

    # Show cluster status
    print("\n🔍 Cluster overview:")
    run("kubectl get pods")
    run("kubectl get svc")

    print("\n🎉 All services deployed successfully!")
    print("Use `minikube service <service-name>` to access individual apps.")

if __name__ == "__main__":
    try:
        main()
    except subprocess.CalledProcessError as e:
        print(f"❌ Command failed: {e}")
        sys.exit(1)
