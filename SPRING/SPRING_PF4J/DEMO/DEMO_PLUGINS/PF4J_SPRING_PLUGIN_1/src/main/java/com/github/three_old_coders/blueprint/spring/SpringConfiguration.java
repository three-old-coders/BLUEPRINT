package com.github.three_old_coders.blueprint.spring;

import com.github.three_old_coders.blueprint.spring.app.SimpleJavaClassB_using_SimpleJavaClassA;
import com.github.three_old_coders.blueprint.spring.app.SimpleJavaClassA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration
{
    @Bean
    public SimpleJavaClassB_using_SimpleJavaClassA hypeMan(final SimpleJavaClassA screamer)
    {
        return new SimpleJavaClassB_using_SimpleJavaClassA(screamer.makeItLoud("plugin 1 is quite nice"));
    }

    @Bean
    public SimpleJavaClassA create()
    {
        return new SimpleJavaClassA();
    }
}
