package com.github.three_old_coders.blueprint.t55bssb.services;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Tapestry55SBLauncher
{
    public static void main(String[] args)
    {
        new SpringApplicationBuilder(Tapestry55SBLauncher.class).web(WebApplicationType.SERVLET).run(args);
    }
}
