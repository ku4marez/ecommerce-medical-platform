#!/bin/bash
set -e

DOCKER_USER="ku4marez"
REPO="myrepo"

SERVICES=(
  "catalog-service:catalog-latest"
  "inventory-service:inventory-latest"
  "order-service:order-latest"
  "payment-service:payment-latest"
  "gateway-service:gateway-latest"
  "admin-client:admin-client-latest"
  "customer-client:customer-client-latest"
)

echo "Logging into Docker Hub..."
docker login

for svc in "${SERVICES[@]}"; do
  NAME="${svc%%:*}"
  TAG="${svc##*:}"
  echo -e "\nBuilding $NAME -> $DOCKER_USER/$REPO:$TAG"
  docker build -t "$DOCKER_USER/$REPO:$TAG" "./services/$NAME" 2>/dev/null || \
  docker build -t "$DOCKER_USER/$REPO:$TAG" "./clients/$NAME" 2>/dev/null || true
  docker push "$DOCKER_USER/$REPO:$TAG"
done

echo -e "\nAll images built and pushed to Docker Hub!"
