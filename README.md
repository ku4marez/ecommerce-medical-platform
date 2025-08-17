# Ecommerce Monorepo (Spring Boot 3 + Java 21 + K8s)

Microservice demo: **catalog**, **order**, **payment**, **inventory**.  
Auth and common libs live in separate repos (reused here).

## Stack
- **Backend:** Spring Boot 3.5.x, Java 21, MongoDB, Kafka, Cache (Caffeine + Redis where needed)
- **Infra:** Docker, Helm, k3s on VPS, NGINX Ingress, cert-manager
- **CI/CD:** GitHub Actions → GHCR → Helm deploy
- **Storage:** MinIO (local) / S3-compatible

## Quickstart (local dev)
1. **Java 21** + **Docker** installed.
2. Spin up infra:
   ```bash
   docker compose -f infra/docker-compose.dev.yml up -d
(MongoDB, Redis, Kafka, MinIO)
3. Build all:

./gradlew build
Run a service (example):

./gradlew :services:catalog-service:bootRun
Service conventions
Package base: com.github.ku4marez.<service>
artifactId / folder: kebab-case

Caching:
auth-service (external): keep stateless JWT; Redis optional for blacklist/rate-limit
Deploy (k3s on VPS)
Build & push images (GH Actions → GHCR)
helm upgrade --install using charts in infra/helm/
Ingress via NGINX + cert-manager (Let’s Encrypt)
Scripts
TBD: make targets or Gradle tasks for compose/helm
---