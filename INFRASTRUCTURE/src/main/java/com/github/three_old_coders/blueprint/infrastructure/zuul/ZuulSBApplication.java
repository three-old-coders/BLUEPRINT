package com.github.three_old_coders.blueprint.infrastructure.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * -Dspring.profiles.active=zuul_service-default -Dlog4j.debug
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class ZuulSBApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(ZuulSBApplication.class, args);
    }
}
