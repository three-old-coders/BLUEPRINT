https://github.com/ragnar-lothbrok/clamav-spring-reactjs
https://medium.com/faun/part2-dockerized-filevirus-detection-service-using-clamav-and-spring-boot-541c689e3634
https://stackoverflow.com/questions/46057625/externalising-spring-boot-properties-when-deploying-to-docker
https://github.com/GoogleContainerTools/jib/blob/master/docs/faq.md#i-need-to-run-commands-like-apt-get
https://spring.io/guides/topicals/spring-boot-docker
https://spring.io/guides/gs/spring-boot-docker/

https://stackoverflow.com/questions/1771679/difference-between-threads-context-class-loader-and-normal-classloader
https://stackoverflow.com/questions/999489/invalid-signature-file-when-attempting-to-run-a-jar/6743609#6743609

------------

mvn package spring-boot:repackage

docker build -t clamav-sb-scanner .
docker image rm clamav-sb-scanner



# docker ps
CONTAINER ID        IMAGE                       COMMAND                  CREATED             STATUS              PORTS                    NAMES
bf504a437a2e        clamav-sb-scanner           "java -Dspring.profi…"   6 seconds ago       Up 5 seconds        3310/tcp, 3316/tcp       stoic_jackson
b197776975e4        arangodb/arangodb:3.7.2.1   "/entrypoint.sh aran…"   3 days ago          Up 3 days           0.0.0.0:8529->8529/tcp   quizzical_rosalind

# docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' bf504a437a2e
172.17.0.3

# docker container inspect bf504a437a2e

docker run -p 3316:3316 clamav-sb-scanner

docker exec -it 63112cf54ddd /bin/sh
