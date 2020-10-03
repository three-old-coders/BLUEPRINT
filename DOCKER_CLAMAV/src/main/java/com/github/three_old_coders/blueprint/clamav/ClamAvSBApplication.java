package com.github.three_old_coders.blueprint.clamav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * -Dspring.profiles.active=clamav_service-default -Dlog4j.debug
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.github.three_old_coders.blueprint.clamav.controller" })
@ConfigurationPropertiesScan
public class ClamAvSBApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(ClamAvSBApplication.class, args);
    }

    // not working as expected. it's not possible to enable/disable services by annotations.
    // different configs still necessary.

    @Configuration
    @Profile("clamav_service-default")
    @EnableEurekaClient
    @EnableDiscoveryClient
    private class EurekaZuulEnvironment
    {
        public EurekaZuulEnvironment() { System.out.println("using Eureka/Zuul environment"); }
    }

    @Configuration
    @Profile("clamav_service-standalone")
    private class StandaloneEnvironment
    {
        public StandaloneEnvironment() { System.out.println("using standalone environment"); }
    }
}
