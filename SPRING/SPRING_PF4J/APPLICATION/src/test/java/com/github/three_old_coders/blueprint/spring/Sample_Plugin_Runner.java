package com.github.three_old_coders.blueprint.spring;

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

        final List<IMessageHandler> extensions = pluginManager.getExtensions(IMessageHandler.class);

        {
            final MessageDesc md = new MessageDesc("P1");
            for (final IMessageHandler extension : extensions) {
                extension.canHandle(md);
            }
        }

        {
            final MessageDesc md = new MessageDesc("P2");
            for (final IMessageHandler extension : extensions) {
                extension.canHandle(md);
            }
        }

        final MessageDesc md = new MessageDesc("P3");
        for (final IMessageHandler extension : extensions) {
            extension.canHandle(md);
        }
    }
}
