package com.github.three_old_coders.blueprint.java_agent;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.Arrays;

public class FileFiltering
    extends File
{
    public FileFiltering(final String pathname)
    {
        super(pathname);
    }

    public FileFiltering(final String parent, final String child)
    {
        super(parent, child);
    }

    public FileFiltering(final File parent, final String child)
    {
        super(parent, child);
    }

    public FileFiltering(final URI uri)
    {
        super(uri);
    }

    // ----

    @Override
    public String[] list(final FilenameFilter filter)
    {
        return filter(super.list(filter));
    }

    @Override
    public File[] listFiles()
    {
        return filter(super.listFiles());
    }

    @Override
    public File[] listFiles(final FilenameFilter filter)
    {
        return filter(super.listFiles(filter));
    }

    @Override
    public File[] listFiles(final FileFilter filter)
    {
        return filter(super.listFiles(filter));
    }

    // ----

    protected boolean accept(final String filename)
    {
        System.out.println(filename);
        return true;
    }

    // ----

    private String[] filter(final String[] vs)
    {
        if (null != vs) {
            return Arrays.stream(vs).filter(this::accept).toArray(new String[]);
        } else {
            return null;
        }
    }

    private File[] filter(final File[] vs)
    {
        if (null != vs) {
            return Arrays.stream(vs).filter(e -> accept(e.getPath() + File.pathSeparator + e.getName())).toArray(new String[]);
        } else {
            return null;
        }
    }
}
