build and push to nexus

    mvn compile jib:build -P distribution

check:

    WebUI: http://localhost:8181/#browse/browse:local-docker-repository

    docker run --rm -e spring.activemq.broker-url=tcp://activemq:61616 \
        --network network-activemq \
        localhost:8182/repository/local-docker-repository/jmspi:0.0.1-SNAPSHOT \
        -c com.github.three_old_coders.blueprint.kubernetes.PiCalculatorJmsConsumerApplication

