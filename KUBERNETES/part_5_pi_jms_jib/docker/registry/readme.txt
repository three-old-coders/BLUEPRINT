registry:

    # generate password file

    curl -X GET test:test@localhost:5000/v2/_catalog

nexus:

    curl -X GET localhost:8182/v2/_catalog --user "test:test"
    curl -X GET test:test@localhost:8182/v2/_catalog

    docker login localhost:8182

        user: test
        password: test

    docker image pull localhost:8182/repository/local-docker-repository/jmspi:0.0.1-SNAPSHOT
    docker image ls | grep 8182

bulk delete images:

    docker container list -a | grep <containername> | awk '{print $1}' | xargs docker container rm

