# Ecommerce Monorepo (Spring Boot 3 + Java 21 + K8s)

Microservice demo: **catalog**, **order**, **payment**, **inventory**.  
Auth and common libs live in separate repos (reused here).

## Stack
- **Backend:** Spring Boot 3.5.x, Java 21, MongoDB, Kafka, Cache (Caffeine + Redis where needed)
- **Infra:** Docker, Helm, k3s on VPS, NGINX Ingress, cert-manager
- **CI/CD:** GitHub Actions → GHCR → Helm deploy
- **Storage:** MinIO (local) / S3-compatible