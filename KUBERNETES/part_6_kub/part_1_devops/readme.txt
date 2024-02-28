
---- PERSISTENCE




---- NEXUS

kubectl create -f nexus-deployment.yaml

kubectl create -f nexus-service.yaml

kubectl describe service
    # ...
    # IP:           10.105.235.191      --> http://10.105.235.191:8181
    # Endpoints:    10.1.0.X:8181
    # ...

kubectl get deployments
kubectl get services

kubectl cluster-info

kubectl get pods
    # nexus-f899bd5c4-rmdqr ...

kubectl logs -f nexus-f899bd5c4-rmdqr
    # ...
    # Started Sonatype...

kubectl exec --stdin --tty nexus -- /bin/bash
cat /nexus-data/admin.password

Browser: localhost:32081
    # follow wizard
    # create repo

    settings --> create repository --> docker (hosted)

        name: local-docker-repository
        http: 8082
        Enable Docker V1 API

        --> create

    settings --> role --> create role

        role type: nexus
        role id: nx-docker
        role name: nx-docker
        role desc: role to access docker images
        privileges: nx-repository-view-*-*-*

    settings --> create user

        id: test
        roles: nx-anonymous, nx-docker

    settings --> security --> realms

        add: docker bearer token realm




---

NAMESPACES

kubectl create namespace nexus
kubectl create namespace devops-tools

kubectl get namespaces

kubectl get deployments
    kubectl get deployments --namespace=nexus
    kubectl delete deployment nexus --namespace=nexus

