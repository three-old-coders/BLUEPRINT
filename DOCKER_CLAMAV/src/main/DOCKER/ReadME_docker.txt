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