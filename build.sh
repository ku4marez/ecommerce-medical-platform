#!/usr/bin/env bash
set -euo pipefail

# Load .env variables if present
if [[ -f ".env" ]]; then
  source .env
fi

if [[ -z "$GPR_USER" || -z "$GPR_TOKEN" ]]; then
  echo "ERROR: Missing GitHub Packages credentials (GPR_USER / GPR_TOKEN)"
  exit 1
fi

DOCKER_USER="ku4marez"
REPO="myrepo"

SERVICES=(
  "catalog"
  "inventory"
  "order"
  "payment"
  "gateway"
)

HELM_DIR="infra/helm"
MANIFESTS_DIR="infra/k8s"

run() {
  echo
  echo ">>> $*"
  eval "$*"
}

echo "=== Starting local k3d dev cluster ==="

# 1. Create or reuse cluster
if k3d cluster get dev-cluster >/dev/null 2>&1; then
  echo "Cluster already running"
else
  run "k3d cluster create dev-cluster --servers 1 --agents 2 --port 8080:80@loadbalancer"
fi

# 2. Build & push service images
for svc in "${SERVICES[@]}"; do
  DOCKERFILE="services/${svc}-service/Dockerfile"

  if [[ ! -f "$DOCKERFILE" ]]; then
    echo "Skipping $svc (Dockerfile not found)"
    continue
  fi

  TAG="${DOCKER_USER}/${REPO}:${svc}-latest"

run "docker build \
  --build-arg GPR_USER=$GPR_USER \
  --build-arg GPR_TOKEN=$GPR_TOKEN \
  -f $DOCKERFILE -t $TAG ."
run "docker push $TAG"
done

# 3. Apply infra manifests (mongo/minio/stripe/kafka etc.)
if [[ -d "$MANIFESTS_DIR" ]]; then
  run "kubectl apply -R -f $MANIFESTS_DIR"
else
  echo "Warning: No infra/k8s directory — skipping manifests"
fi

# 4. Deploy backend using Helm
for svc in "${SERVICES[@]}"; do
  CHART="${HELM_DIR}/${svc}-service"
  if [[ ! -d "$CHART" ]]; then
    echo "Skipping Helm for $svc (chart missing)"
    continue
  fi

  run "helm upgrade --install $svc $CHART \
    --set image.repository=${DOCKER_USER}/${REPO} \
    --set image.tag=${svc}-latest"
done

# 5. Display cluster state
run "kubectl get pods -A"
run "kubectl get svc -A"

echo
echo "=== Deployment complete ==="
echo "Visit: http://localhost:8080"

