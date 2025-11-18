# Ecommerce Monorepo (Spring Boot 3 + Java 21 + K8s)

Microservice demo: **catalog**, **order**, **payment**, **inventory**.  
Auth and common libs live in separate repos (reused here).

## Stack
- **Backend:** Spring Boot 3.5.x, Java 21, MongoDB, Kafka, Cache (Caffeine + Redis where needed)
- **Infra:** Docker, Helm, k3s on VPS, NGINX Ingress, cert-manager
- **CI/CD:** GitHub Actions → GHCR → Helm deploy
- **Storage:** MinIO (local) / S3-compatible

# Kubernetes basics
- kubectl cluster-info
- kubectl get nodes
- kubectl get pods -A
- kubectl get svc -A
- kubectl get deploy -A
- kubectl get ns

- kubectl create namespace myns
- kubectl delete namespace myns

- kubectl apply -f file.yaml
- kubectl apply -R -f infra/k8s
- kubectl delete -f file.yaml

- kubectl logs <pod> -n <ns>
- kubectl logs <pod> -n <ns> -f
- kubectl describe pod <pod> -n <ns>
- kubectl exec -it <pod> -n <ns> -- bash

- kubectl get events -A --sort-by=.metadata.creationTimestamp

- helm upgrade --install app chart/
- helm uninstall app
- helm list -A

- k3d cluster create dev --servers 1 --agents 2 --port 8080:80@loadbalancer
- k3d cluster delete dev
- k3d cluster list
- k3d cluster start dev
- k3d cluster stop dev
- kubectl port-forward svc/my-service 8081:80 -n default

# Run build script (git bash)
- chmod +x build.sh
- ./build.sh
