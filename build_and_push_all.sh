#!/bin/bash
set -e

DOCKER_USER="ku4marez"
REPO="myrepo"
GPR_USER="ku4marez"
GPR_TOKEN="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"

# Define all services
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
  IMAGE="$DOCKER_USER/$REPO:$TAG"

  # Determine where the Dockerfile is located
  if [[ -f "services/$NAME/Dockerfile" ]]; then
    DOCKERFILE="services/$NAME/Dockerfile"
    echo "Building backend service $NAME -> $IMAGE"
    docker build \
      -f "$DOCKERFILE" \
      -t "$IMAGE" \
      --build-arg GPR_USER="$GPR_USER" \
      --build-arg GPR_TOKEN="$GPR_TOKEN" \
      .
  elif [[ -f "clients/$NAME/Dockerfile" ]]; then
    DOCKERFILE="clients/$NAME/Dockerfile"
    echo "Building frontend $NAME -> $IMAGE"
    docker build -f "$DOCKERFILE" -t "$IMAGE" .
  else
    echo "Skipping $NAME (Dockerfile not found)"
    continue
  fi

  echo "Pushing $IMAGE"
  docker push "$IMAGE"
done

echo "All images built and pushed successfully."
