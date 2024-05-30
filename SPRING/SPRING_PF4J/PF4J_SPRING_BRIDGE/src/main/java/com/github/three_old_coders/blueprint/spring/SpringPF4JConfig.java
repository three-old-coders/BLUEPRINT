package com.github.three_old_coders.blueprint.spring;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.pf4j.DefaultExtensionFactory;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ExtensionFactory;
import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class SpringPF4JConfig
    implements ISpringPF4JConfigBase
{
    private ApplicationContext _ac = null;

    /**
     * allows access to spring beans without using @Autowired
     * @param ac
     */
    @SneakyThrows
    @Override
    public void setApplicationContext(final ApplicationContext ac)
    {
        _ac = ac;
    }

    @Override
    @Bean
    public PluginManager getPluginManager()
    {
        final List<Path> paths;
        if (! "development".equals(System.getProperty("pf4j.mode"))) {
            final String pf4jPluginPaths = _ac.getEnvironment().getProperty("pf4j-plugin-paths");

            final RegexFileFilter rffName = new RegexFileFilter(".*-all\\.jar");
            final RegexFileFilter rffDir = new RegexFileFilter(".*");

            final Collection<File> files = FileUtils.listFiles(new File(pf4jPluginPaths), rffName, rffDir);
            paths = files.stream().map(file -> file.toPath().getParent()).toList();
        } else {
            paths = new ArrayList<>();
        }

        final PluginManager pluginManager = new DefaultPluginManager(paths)
        {
            @Override
            protected ExtensionFactory createExtensionFactory()
            {
                return new DefaultExtensionFactory()
                {
                    @SneakyThrows
                    @Override
                    public <T> T create(final Class<T> extensionClass)
                    {
                        try {
                            final Constructor<T> constructor = extensionClass.getConstructor(ApplicationContext.class);
                            return constructor.newInstance(_ac);
                        } catch (final Exception e) {
                            // non spring class
                            return super.create(extensionClass);
                        }
                    }
                };
            }
        };

        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        return pluginManager;
    }
}
