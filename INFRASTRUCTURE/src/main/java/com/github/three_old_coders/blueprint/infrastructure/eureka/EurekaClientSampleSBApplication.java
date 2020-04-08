package com.github.three_old_coders.blueprint.infrastructure.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaClient
public class EurekaClientSampleSBApplication
{
    @Autowired private Environment _env;

    public static void main(final String[] args)
    {
        SpringApplication.run(EurekaClientSampleSBApplication.class, args);
    }
}
