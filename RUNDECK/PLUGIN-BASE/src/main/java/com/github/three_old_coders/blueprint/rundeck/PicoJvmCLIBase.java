package com.github.three_old_coders.blueprint.rundeck;

import picocli.CommandLine;

public abstract class PicoJvmCLIBase
    extends PicoCLIBase
{
    @CommandLine.Option(names = {"-jvm"}, description = "java settings (defaults to -Xss4m. Surround with \" to handle multiple args)", defaultValue = "-Xss4m")
    private String jvmSettings;
}
