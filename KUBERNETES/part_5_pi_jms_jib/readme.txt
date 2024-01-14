build and push to nexus

    mvn compile jib:build -P distribution

check:

    http://localhost:8181/#browse/browse:local-docker-repository

