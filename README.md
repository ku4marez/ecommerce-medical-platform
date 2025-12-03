# Ecommerce Monorepo (Spring Boot 3 + Java 21 + Kubernetes/Helm)

Microservice demo: **catalog**, **order**, **payment**, **inventory**.  
Auth and common libs live in separate repos (reused here).

## Stack
- **Backend:** Spring Boot 3.5.x, Java 21, MongoDB, Kafka, Redis cache, Resilience, API Gateway
- **Infra:** Docker, Helm, k3d locally on top of WSL
- **CI/CD:** GitHub Actions → Docker hub
- **Storage:** MinIO (local) / S3-compatible

# Kubernetes basics
- kubectl config get-contexts                                   # list configured clusters
- kubectl config use-context <context>                          # switch cluster
- kubectl get nodes                                             # list cluster nodes
- kubectl get pods -n <namespace>                               # list pods in namespace
- kubectl get deployments -n <namespace>                        # list deployments
- kubectl get services -n <namespace>                           # list services
- kubectl get all -n <namespace>                                # list all resources
- kubectl scale deployment <name> --replicas=<n> -n <namespace> # scale deployment

# Delete resources
- kubectl delete deployment <name> -n <ns>                     # delete deployment
- kubectl delete pod <pod> -n <ns>                             # delete pod
- kubectl delete svc <svc> -n <ns>                             # delete service
- kubectl delete pvc <pvc> -n <ns>                             # delete persistent volume claim

# Rollouts / inspection
- kubectl rollout restart deployment/<name> -n <ns>            # restart deployment
- kubectl get <resource> <name> -o yaml                        # view resource YAML
- kubectl rollout status deployment/<name> -n <ns>             # wait for rollout to finish

# Namespaces
- kubectl create namespace myns                                # create namespace
- kubectl delete namespace myns                                # delete namespace

# Apply / remove manifests
- kubectl apply -f file.yaml                                   # apply manifest
- kubectl apply -R -f infra/k8s                                # apply directory recursively
- kubectl delete -f file.yaml                                  # delete via manifest

# Logs / exec / describe
- kubectl logs <pod> -n <ns>                                   # show logs
- kubectl logs <pod> -n <ns> -f                                # follow logs
- kubectl describe pod <pod> -n <ns>                           # describe pod details
- kubectl exec -it <pod> -n <ns> -- bash                       # open shell inside pod
- kubectl set image deployment/x container=y:image             # update deployment image

# Debug / metrics
- kubectl get events -A --sort-by=.metadata.creationTimestamp  # list events (chronological)
- kubectl top pods -n <ns>                                     # pod cpu/mem usage
- kubectl top nodes                                            # node cpu/mem usage
- kubectl explain <resource>                                   # show resource schema
- kubectl describe node <node>                                 # node info / issues

# Helm
- helm upgrade --install app chart/                            # install or upgrade release
- helm uninstall app                                           # uninstall release
- helm list -A                                                 # list all releases

# k3d local cluster
- k3d cluster create dev --servers 1 --agents 2 --port 8080:80@loadbalancer   # create k3d cluster
- k3d cluster delete dev                                       # delete cluster
- k3d cluster list                                             # list clusters
- k3d cluster start dev                                        # start cluster
- k3d cluster stop dev                                         # stop cluster

# Networking / forwarding
- kubectl port-forward svc/my-service 8081:80 -n default       # forward local port to service

# Run build script (git bash)
- chmod +x build.sh
- ./build.sh

# Git basics
- git clone git@github.com:ku4marez/ecommerce-medical-platform.git                    # download repo that doesn't exist locally
- git remote add origin git@github.com:ku4marez/ecommerce-medical-platform.git        # attach remote to existing local repo
- git status                                # check what changed (changed)
- git switch -c feature/name                # create new feature branch
- git switch <branch>                       # switch branches
- git merge feature/name                    # merge into curr branch
- git add -p                                # stage changes interactively
- git commit -m "msg"                       # commit
- git fetch                                 # update remote refs
- git pull --rebase origin main             # update branch with main (clean history)
- git push -u origin feature/name           # push new branch
- git push --force-with-lease               # push after rebase (safe force)
- git diff                                  # view unstaged changes
- git diff --staged                         # view staged changes
- git restore .                             # discard local unstaged edits
- git reset --hard                          # reset everything to last commit
- git reset HEAD~1                          # remove last commit (keep files)
- git stash                                 # stash current changes
- git stash pop                             # apply & remove stash
- git branch                                # list branches
- git branch -D feature/name                # delete local branch
- git push origin --delete feature/name     # delete remote branch
- git rebase --continue                     # continue after conflict resolution
- git rebase --abort                        # stop rebase if things go wrong
- git log --oneline --graph --decorate      # compact commit graph
- git reflog                                # shows every place HEAD pointed in the past
- git cherry-pick <commit>                  # apply commit from other branch
- git cherry-pick --continue                # continue after conflicts
- git fetch --prune                         # clean up local state branches

# .gitingore handling. Remove tracked files after adding to gitignore
- git rm --cached -r .
- git add .
- git commit -m "Clean ignored files"

# navigation
- ls                     # list files
- ls -la                 # list all w/ details
- cd <dir>               # change directory
- cd ..                  # go up
- pwd                    # current path

# files & dirs
- mkdir <dir>            # make directory
- mkdir -p a/b/c         # nested dirs
- touch <file>           # create empty file
- cp <src> <dst>         # copy file
- cp -r <src> <dst>      # copy directory
- mv <src> <dst>         # move/rename
- rm <file>              # delete file
- rm -r <dir>            # delete directory
- rm -rf <dir>           # delete w/o prompts
- ln -s <src> <dst>      # create symbolink links between 2 files (use absolute path, -f file <dst> exists otherwise error)

# view / edit
- cat <file>             # print file
- less <file>            # scroll view
- head -n <file>         # first n lines
- tail -n <file>         # last n lines
- tail -f <file>         # live log
- nano <file>            # edit (easy)
- vim <file>             # edit (advanced)
- code <file>            # open in VS Code (WSL)
- sed -i 's/toReplace/replacer/g' <file> # replace string match in file

# search
- grep "txt" <file>      # search in file
- grep -R "txt" .        # recursive search
- grep -n "txt" <file>   # with line numbers
- grep -c "txt" <file>   # count how many times found

# find
- find . -name "*.yml"     # find by name
- find . -type f -name "x" # find files
- find . -name "*Zone*"    # match by pattern

# permissions
- chmod +x file.sh       # make executable
- chmod 700 file         # make accessible to me
- chmod 644 file         # rw-r--r--
- chmod 755 file         # rwxr-xr-x
- chown -R <user> <file  # change ownership             

# processes
- ps aux                 # list processes
- top                    # live view
- htop                   # better top
- kill <pid>             # kill process
- kill -9 <pid>          # force kill
- ss -ltnp               # show network connections
- lsof -i :port          # check what process is using port

# networking
- curl <url>             # fetch
- wget <url>             # download
- ping google.com        # test network
- netstat -tulpn         # ports
- nslookup google.com    # check dns resolution

# system info
- uname -a               # system info
- df -h                  # disk usage
- du -sh <dir>           # folder size
- free -h                # RAM

# archive
- tar -czf a.tar.gz dir/ # create tar.gz
- tar -xzf a.tar.gz      # extract
- unzip file.zip         # unzip

# piping & redirects
- cmd > f                # overwrite file
- cmd >> f               # append
- cmd | grep "x"         # filter
- cmd | less             # scroll

# wsl management
- wsl --list             # list distros
- wsl --shutdown         # stop WSL VM

# repo access with ssh
- git config --global user.name "ku4ma"
- git config --global user.email "ku4marez@gmail.com"
- ssh-keygen -t ed25519 -C "ku4marez@gmail.com"
- cat ~/.ssh/id_ed25519.pub
- ssh -T git@github.com

# precommit local setup
- sudo apt update
- sudo apt install pre-commit
- pre-commit run --all-files
- pre-commit autoupdate

# redis bitnami cluster
- helm repo add bitnami https://charts.bitnami.com/bitnami
- helm install redis bitnami/redis --namespace redis --create-namespace

# metrics 
- helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
- helm install monitoring prometheus-community/kube-prometheus-stack
- admin/BpWD5aPs32X1R1Gg04QjvOPmAqFzscfMLXNI2s5d

# WSL install AWS CLI, Helm, kubectl, k3d, Java 21, git
- sudo apt update && sudo apt upgrade -y
- sudo apt install -y git
- sudo apt install -y openjdk-21-jdk
- curl -s https://raw.githubusercontent.com/k3d-io/k3d/main/install.sh | bash
- curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
- chmod +x kubectl
- sudo mv kubectl /usr/local/bin/
- curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash
- curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
- sudo apt install -y unzip
- unzip awscliv2.zip
- sudo ./aws/install
- rm -rf aws awscliv2.zip

## Gradle
- ./gradlew build                    # Build project
- ./gradlew test                     # Run tests
- ./gradlew clean                    # Clean build directory
- ./gradlew bootRun                  # Run Spring Boot application
- ./gradlew jibDockerBuild           # Build Docker image (if using Jib)
- ./gradlew tasks                    # List tasks

## Maven
- ./mvnw clean package               # Build project
- ./mvnw test                        # Run tests
- ./mvnw clean                       # Clean build directory
- ./mvnw spring-boot:run             # Run Spring Boot application
- ./mvnw clean package -DskipTests`  # Skip tests build
- ./mvnw install                     # Install artifact to local repo

# Docker basics
- docker version                # show Docker version info
- docker info                   # system-wide info
- docker ps                     # running containers
- docker ps -a                  # all containers
- docker images                 # list local images
- docker pull <image>           # download image
- docker search <name>          # search image on Docker Hub

# Running containers
- docker run <image>            # run in foreground
- docker run -d <image>         # run detached
- docker run -it <image> bash   # interactive shell
- docker run -p 8080:80 <img>   # port mapping
- docker run -e KEY=VAL <img>   # env vars

# Container management
- docker stop <id>              # stop container
- docker start <id>             # start container
- docker restart <id>           # restart
- docker kill <id>              # force kill
- docker rm <id>                # remove container
- docker rm -f <id>             # remove running container
- docker prune                  # clean unused objects

# Logs / exec
- docker logs <id>              # show logs
- docker logs -f <id>           # follow logs
- docker exec -it <id> bash     # enter running container

# Images
- docker build -t name:tag .    # build image
- docker tag src:tag dest:tag   # re-tag image
- docker rmi <image>            # remove image
- docker rmi -f <image>         # force remove
- docker history <image>        # show layers

# Volumes
- docker volume ls              # list volumes
- docker volume inspect <vol>   # inspect volume
- docker volume rm <vol>        # remove volume

# Networks
- docker network ls             # list networks
- docker network inspect <net>  # inspect
- docker network rm <net>       # delete

# Compose (if using docker-compose)
- docker compose up -d          # start services
- docker compose down           # stop + remove
- docker compose logs -f        # watch logs
- docker compose ps             # status
- docker compose build          # rebuild

# AWS Configure / Profiles
- aws configure                                                # set default profile
- aws configure --profile dev                                  # create/use "dev" profile
- aws sts get-caller-identity                                  # show current IAM identity
- aws configure list                                           # show loaded credentials

# EC2
- aws ec2 describe-instances                                   # list all EC2 instances
- aws ec2 describe-instance-status --instance-id <id>          # instance status
- aws ec2 start-instances --instance-ids <id>                  # start instance
- aws ec2 stop-instances --instance-ids <id>                   # stop instance
- aws ec2 reboot-instances --instance-ids <id>                 # reboot instance
- aws ec2 terminate-instances --instance-ids <id>              # delete instance
- aws ec2 describe-security-groups                             # list security groups
- aws ec2 describe-vpcs                                        # list VPCs
- aws ec2 describe-subnets                                     # list subnets

# S3 (Storage)
- aws s3 ls                                                    # list buckets
- aws s3 ls s3://bucket                                        # list objects in bucket
- aws s3 cp file.txt s3://bucket/dir/                          # upload file
- aws s3 cp s3://bucket/file.txt .                             # download file
- aws s3 sync ./localdir s3://bucket/dir/                      # sync directory
- aws s3 rm s3://bucket/file.txt                               # delete object

# CloudWatch Logs
- aws logs describe-log-groups                                  # list log groups
- aws logs describe-log-streams --log-group-name <name>         # list streams
- aws logs tail <log-group> --follow                            # tail logs (follow mode)

# ECS (Containers)
- aws ecs list-clusters                                               # list ECS clusters
- aws ecs list-services --cluster <cluster>                           # list services
- aws ecs describe-services --cluster <cluster> --services <service>  # describe service
- aws ecs list-tasks --cluster <cluster> --service-name <service>     # list tasks
- aws ecs describe-tasks --cluster <cluster> --tasks <task-id>        # task details
- aws ecs update-service --cluster <cluster> --service <service> --force-new-deployment # restart service

# ECR (Container Registry)
- aws ecr get-login-password | docker login --username AWS --password-stdin <registry> # login to ECR
- aws ecr create-repository --repository-name <repo>            # create repo
- aws ecr describe-repositories                                 # list repos
- aws ecr list-images --repository-name <repo>                  # list images

# Lambda
- aws lambda list-functions                                     # list functions
- aws lambda invoke --function-name <name> out.json             # run lambda
- aws lambda update-function-code --function-name <name> --zip-file fileb://build.zip # update code
- aws lambda get-function --function-name <name>                # details

# DynamoDB
- aws dynamodb list-tables                                      # list tables
- aws dynamodb scan --table-name <table>                        # full scan
- aws dynamodb query --table-name <table> --key-condition-expression "<expr>" --expression-attribute-values <json> # query

# RDS
- aws rds describe-db-instances                                 # list DB instances
- aws rds describe-db-clusters                                  # list clusters
- aws rds reboot-db-instance --db-instance-identifier <id>      # restart instance

# SSM Parameter Store
- aws ssm get-parameter --name <path> --with-decryption         # read secret
- aws ssm put-parameter --name <path> --value <val> --type SecureString --overwrite # write secret
- aws ssm delete-parameter --name <path>                        # delete secret

# Secrets Manager
- aws secretsmanager list-secrets                               # list secrets
- aws secretsmanager get-secret-value --secret-id <id>          # read secret
- aws secretsmanager put-secret-value --secret-id <id> --secret-string <json> # write secret value

# SQS
- aws sqs list-queues                                           # list queues
- aws sqs receive-message --queue-url <url>                     # read messages
- aws sqs send-message --queue-url <url> --message-body "Hi"    # send
- aws sqs delete-message --queue-url <url> --receipt-handle <handle> # delete

# SNS
- aws sns list-topics                                           # list topics
- aws sns publish --topic-arn <arn> --message "Hello"           # publish message

# IAM
- aws iam get-user                                              # current IAM user
- aws iam list-roles                                            # roles
- aws iam list-users                                            # users
- aws iam list-policies                                         # policies

# SSO
- aws configure sso                                             # configure SSO profile
- aws sso login --profile cts-dev                               # login to SSO
- aws sts get-caller-identity --profile cts-dev                 # verify SSO identity

# CloudFormation
- aws cloudformation list-stacks                                # list stacks
- aws cloudformation describe-stacks --stack-name <name>        # details
- aws cloudformation delete-stack --stack-name <name>           # delete stack

# CodePipeline / CodeBuild
- aws codepipeline list-pipelines                               # list pipelines
- aws codepipeline start-pipeline-execution --name <name>       # trigger pipeline
- aws codepipeline get-pipeline-state --name <name>             # pipeline status

# STS (Temp Credentials)
- aws sts assume-role --role-arn <arn> --role-session-name session1 # assume role

# Regions
- aws ec2 describe-regions                                      # list AWS regions

# EKS (Kubernetes)
- aws eks list-clusters --region <region>                       # list EKS clusters
- aws eks describe-cluster --name <cluster> --region <region>   # cluster details
- aws eks update-kubeconfig --name <cluster> --region <region> --profile <profile> # add cluster to kubectl
- aws eks update-kubeconfig --name <cluster> --region <region> --alias <alias> # add with custom context name
