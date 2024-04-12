package com.github.three_old_coders.blueprint.spring_basic;

import com.github.three_old_coders.blueprint.spring.ISpringPF4JConfigBase;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@Configuration
public class SpringTestConfig
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
        final String pf4jPluginPaths = _ac.getEnvironment().getProperty("pf4j-plugin-paths");

        final RegexFileFilter rffName = new RegexFileFilter(".*-all\\.jar");
        final RegexFileFilter rffDir = new RegexFileFilter(".*");

        final Collection<File> files = FileUtils.listFiles(new File(pf4jPluginPaths), rffName, rffDir);
        final List<Path> pathes = files.stream().map(file -> file.toPath().getParent()).toList();

        final PluginManager pluginManager = new DefaultPluginManager(pathes);
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        return pluginManager;
    }
}
