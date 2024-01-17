----- DOCKER REGISTRY

kubectl create secret docker-registry imageregistry \
    --docker-server=localhost:5000 \
    --docker-username=test --docker-password=testpw --docker-email=XXX

    # secret/imageregistry created

kubectl create serviceaccount registrysa

    # serviceaccount/registrysa created

kubectl patch serviceaccount registrysa -p '{"imagePullSecrets": [{"name": "imageregistry"}]}'

    # serviceaccount/registrysa patched

kubectl apply -f registry-deployment.yaml

    # deployment.apps/imageregistry-deployment created
    # deployment.apps/imageregistry-deployment configured









----- ACTIVE MQ

kubectl create -f activemq-deployment.yaml          create/replace
kubectl create -f activemq-service.yaml

kubectl describe services activemq
kubectl describe pods activemq
kubectl get pods
kubectl get pods --no-headers -o custom-columns=":metadata.name"

    NAME                        READY   STATUS    RESTARTS   AGE
    activemq-599b4558b9-9h95g   1/1     Running   0          30m

kubectl port-forward <pod-name> <local-port>:<remote-port>
kubectl port-forward activemq-599b4558b9-9h95g 61616:61616
kubectl port-forward activemq-599b4558b9-9h95g 61616:61616 &

    Forwarding from 127.0.0.1:61616 -> 61616
    Forwarding from [::1]:61616 -> 61616

        ps -ef | grep port-forward
        kill -9 XXX

-----


https://www.middlewareinventory.com/blog/kubectl-port-forward/
https://stackoverflow.com/questions/59296801/docker-compose-exit-code-is-137-when-there-is-no-oom-exception
https://blog.knoldus.com/how-to-use-nexus-3-as-private-docker-registry/