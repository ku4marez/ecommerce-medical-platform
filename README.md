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

# Git basics
- git status                                # check what changed (changed)
- git switch -c feature/name                # create new feature branch
- git switch <branch>                       # switch branches
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

# view / edit
- cat <file>             # print file
- less <file>            # scroll view
- head <file>            # first lines
- tail <file>            # last lines
- tail -f <file>         # live log
- nano <file>            # edit (easy)
- vim <file>             # edit (advanced)
- code <file>            # open in VS Code (WSL)

# search
- grep "txt" <file>      # search in file
- grep -R "txt" .        # recursive search
- grep -n "txt" <file>   # with line numbers

# find
- find . -name "*.yml"   # find by name
- find . -type f -name "x" # find files
- find . -name "*Zone*"  # match by pattern

# permissions
- chmod +x file.sh       # make executable
- chmod 644 file         # rw-r--r--
- chmod 755 file         # rwxr-xr-x

# processes
- ps aux                 # list processes
- top                    # live view
- htop                   # better top
- kill <pid>             # kill process
- kill -9 <pid>          # force kill

# networking
- curl <url>             # fetch
- wget <url>             # download
- ping google.com        # test network
- netstat -tulpn         # ports

# system info
- uname -a               # system info
- df -h                  # disk usage
- du -sh <dir>           # folder size
- free -h                # RAM

# archive
- tar -czf a.tar.gz dir/ # create tar.gz
- tar -xzf a.tar.gz      # extract
- unzip file.zip         # unzip

# package management (Ubuntu/WSL)
- sudo apt update        # refresh packages
- sudo apt install pkg   # install
- sudo apt remove pkg    # remove

# piping & redirects
- cmd > f                # overwrite file
- cmd >> f               # append
- cmd | grep "x"         # filter
- cmd | less             # scroll

# wsl management
- wsl --list             # list distros
- wsl --shutdown         # stop WSL VM

# repo access with ssh
- ssh-keygen -t ed25519 -C "ku4marez@gmail.com"
- cat ~/.ssh/id_ed25519.pub
- ssh -T git@github.com

# precommit local setup
- sudo apt update
- sudo apt install pre-commit
- pre-commit run --all-files
- pre-commit autoupdate
