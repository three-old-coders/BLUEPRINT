package com.github.three_old_coders.blueprint.spring;

import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

public interface ISpringPF4JConfigBase
    extends ApplicationContextAware
{
    @Bean PluginManager getPluginManager();
}
