package com.github.three_old_coders.blueprint.clamav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * -Dspring.profiles.active=clamav_service-default -Dlog4j.debug
 */
@SpringBootApplication
// @EnableEurekaClient
// @EnableDiscoveryClient
@ComponentScan(basePackages = { "com.github.three_old_coders.blueprint.clamav.controller" })
@ConfigurationPropertiesScan
public class ClamAvSBApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(ClamAvSBApplication.class, args);
    }
}
