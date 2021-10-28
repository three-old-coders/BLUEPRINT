package com.github.three_old_coders.blueprint.rundeck;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(
    description = "test Pico CLI class", name = "Runner_PicoSampleCLI_2",
    mixinStandardHelpOptions = true, version = "0.1"
)
public class Runner_PicoSampleCLI_2
    extends PicoCLIBase
{
    @CommandLine.Parameters(index = "1", description = "result file B")
    private File resultFileB;

    @CommandLine.Parameters(index = "0", description = "result file A")
    private File resultFileA;

    @CommandLine.Option(names = {"-apikey"}, description = "ApiKey", required = true)
    private String apiKey;

    @CommandLine.Option(names = {"-bool"}, description = "boolean type", required = true, defaultValue = "true")
    private boolean boolAttr;

    @CommandLine.Option(names = {"-inFile"}, description = "file to read", required = true, defaultValue = "default_in_file")
    private File inputFile;

    @CommandLine.Option(names = {"-optionalFlag"}, description = "optional flag")
    private File optionalFlag;

    // ----

    public static void main(final String[] args)
    {
        System.exit(new CommandLine(new Runner_PicoSampleCLI_2()).execute(args));
    }

    @Override public Integer call()
    {
        return 0;
    }
}
