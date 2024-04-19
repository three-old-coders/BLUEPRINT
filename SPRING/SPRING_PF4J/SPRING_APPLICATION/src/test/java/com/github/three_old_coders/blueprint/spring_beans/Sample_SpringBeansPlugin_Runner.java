package com.github.three_old_coders.blueprint.spring_beans;

import com.github.three_old_coders.blueprint.RunMethodUtils;
import com.github.three_old_coders.blueprint.spring.app.SpringBeanA;
import lombok.SneakyThrows;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.github.three_old_coders.blueprint.spring.app",
    "com.github.three_old_coders.blueprint.spring.pf4j",
    "com.github.three_old_coders.blueprint.spring_common"
})
@PropertySource("classpath:config/pf4j.properties")
public class Sample_SpringBeansPlugin_Runner
    implements CommandLineRunner
{
    public static void main(final String[] args)
    {
        SpringApplication.run(Sample_SpringBeansPlugin_Runner.class, args);
    }

    // ----

    @Autowired private PluginManager _pm;

    @Autowired private SpringBeanA _sba;

    @SneakyThrows
    public void run(final String... args)
    {
        System.out.println(_sba.giveMeAnAAAAA() + " " + _sba);

        RunMethodUtils.check_canHandle_1_2(_pm);
    }
}
