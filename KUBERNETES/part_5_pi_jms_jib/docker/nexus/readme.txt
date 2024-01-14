start and wait

docker-compose up -d
docker ps
docker logs nexus-nexus-1

docker exec -it nexus-nexus-1 cat /nexus-data/admin.password

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





