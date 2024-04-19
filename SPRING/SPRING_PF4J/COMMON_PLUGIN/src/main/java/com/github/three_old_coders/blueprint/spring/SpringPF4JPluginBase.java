package com.github.three_old_coders.blueprint.spring;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.pf4j.Plugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * is this helpful at all?
 */
public abstract class SpringPF4JPluginBase
    extends Plugin
{
    @Accessors(prefix = "_")
    public static abstract class SpringExtensionBase
    {
        @Getter private final AnnotationConfigApplicationContext _applicationContext;

        public SpringExtensionBase(final ApplicationContext applicationContext)
        {
            _applicationContext = new AnnotationConfigApplicationContext();
            _applicationContext.setParent(applicationContext);
            _applicationContext.setClassLoader(this.getClass().getClassLoader());
            _applicationContext.scan(getPluginScanPackages());
            _applicationContext.refresh();
        }

        /**
         * simulates ComponentScan annotation
         * @return packages to scan
         */
        protected abstract String[] getPluginScanPackages();
    }
}
