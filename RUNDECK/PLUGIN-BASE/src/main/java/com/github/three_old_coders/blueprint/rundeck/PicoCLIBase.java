package com.github.three_old_coders.blueprint.rundeck;

import picocli.CommandLine;

import java.util.Map;
import java.util.concurrent.Callable;

public abstract class PicoCLIBase
    implements Callable<Integer>
{
    @SuppressWarnings("unused")
    @CommandLine.Option(names = "-D", mapFallbackValue = "") // allow -Dkey
    void setProperty(final Map<String, String> props)
    {
        props.forEach(System::setProperty);
    }
}
