#!/usr/bin/env bash
set -euo pipefail

# Load .env
if [[ -f ".env" ]]; then
  source .env
fi

if [[ -z "${GPR_USER:-}" || -z "${GPR_TOKEN:-}" ]]; then
  echo "ERROR: Missing GitHub Packages credentials (GPR_USER / GPR_TOKEN)"
  exit 1
fi

DOCKER_USER="ku4marez"
REPO="myrepo"

SERVICES=(catalog inventory order payment gateway)

HELM_DIR="infra/helm"
MANIFESTS_DIR="infra/k8s"

run() {
  echo
  echo ">>> $*"
  eval "$*"
}

echo "=== Starting local k3d dev cluster ==="

# Create or reuse cluster
if ! k3d cluster get dev-cluster >/dev/null 2>&1; then
  run "k3d cluster create dev-cluster --servers 1 --agents 2 --port 8080:80@loadbalancer"
else
  echo "Cluster already running"
fi

# Single unique timestamp for ALL services
BUILD_ID=$(date +%s)

# Build & push images
for svc in "${SERVICES[@]}"; do
  DOCKERFILE="services/${svc}-service/Dockerfile"

  if [[ ! -f "$DOCKERFILE" ]]; then
    echo "Skipping $svc (Dockerfile not found)"
    continue
  fi

  IMAGE="${DOCKER_USER}/${REPO}:${svc}-${BUILD_ID}"

  run "docker build \
    --build-arg GPR_USER=$GPR_USER \
    --build-arg GPR_TOKEN=$GPR_TOKEN \
    --build-arg CACHE_BUSTER=$BUILD_ID \
    -f $DOCKERFILE -t $IMAGE ."

  run "docker push $IMAGE"
done

# Infra manifests
if [[ -d "$MANIFESTS_DIR" ]]; then
  run "kubectl apply -R -f $MANIFESTS_DIR"
fi

# Install or upgrade Redis via Helm (Bitnami)
REDIS_NS="redis"

if ! helm status redis -n "$REDIS_NS" >/dev/null 2>&1; then
  echo ">>> Redis not found — installing Redis"
  run "helm repo add bitnami https://charts.bitnami.com/bitnami"
  run "helm repo update"
  run "helm install redis bitnami/redis \
        --namespace $REDIS_NS \
        --create-namespace"
else
  echo ">>> Redis exists — upgrading"
  run "helm upgrade redis bitnami/redis -n $REDIS_NS"
fi

if ! helm status monitoring -n >/dev/null 2>&1; then
  echo ">>> Metrics not found — installing Prometheus/Grafana"
  run "helm repo add prometheus-community https://prometheus-community.github.io/helm-charts"
  run "helm repo update"
  run "helm install monitoring prometheus-community/kube-prometheus-stack"
else
  echo ">>> Redis exists — upgrading"
  run "helm upgrade monitoring  prometheus-community/kube-prometheus-stack"
fi

# Deploy via Helm
for svc in "${SERVICES[@]}"; do
  CHART="${HELM_DIR}/${svc}-service"
  if [[ ! -d "$CHART" ]]; then
    echo "Skipping Helm for $svc (chart missing)"
    continue
  fi

  IMAGE_TAG="${svc}-${BUILD_ID}"

  run "helm upgrade --install $svc $CHART \
    --set image.repository=${DOCKER_USER}/${REPO} \
    --set image.tag=${IMAGE_TAG}"
done

# Show cluster state
run "kubectl get pods -A"
run "kubectl get svc -A"

echo
echo "=== Deployment complete ==="
echo "Visit: http://localhost:8080"
