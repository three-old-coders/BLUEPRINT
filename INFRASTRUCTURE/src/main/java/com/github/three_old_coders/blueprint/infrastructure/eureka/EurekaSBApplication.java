package com.github.three_old_coders.blueprint.infrastructure.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

/**
 * -Dspring.profiles.active=eureka_service-default -Dlog4j.debug
 */
@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = { "com.github.three_old_coders.blueprint.infrastructure.eureka.controller" })
public class EurekaSBApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(EurekaSBApplication.class, args);
    }
}
