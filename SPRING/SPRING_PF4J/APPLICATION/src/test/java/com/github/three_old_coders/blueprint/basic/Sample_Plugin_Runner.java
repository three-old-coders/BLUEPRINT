package com.github.three_old_coders.blueprint.basic;

import com.github.three_old_coders.blueprint.RunMethodUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class Sample_Plugin_Runner
{
    @SneakyThrows
    public static void main(String[] args)
    {
        final RegexFileFilter rffName = new RegexFileFilter(".*-all\\.jar");
        final RegexFileFilter rffDir = new RegexFileFilter(".*");

        final Collection<File> files = FileUtils.listFiles(new File(args[0]), rffName, rffDir);
        final List<Path> pathes = files.stream().map(file -> file.toPath().getParent()).toList();

        final PluginManager pluginManager = new DefaultPluginManager(pathes);
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        RunMethodUtils.check_canHandle_1_2(pluginManager);
    }
}
