package com.github.three_old_coders.blueprint.rundeck;

import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(
    description = "test Picoli CLI class", name = "Runner_PicoliSampleCLI",
    mixinStandardHelpOptions = true, version = "0.1"
)
public class Runner_PicoliSampleCLI
    implements Callable<Integer>
{
    @CommandLine.Parameters(index = "1", description = "result file B")
    private File resultFileB;

    @CommandLine.Parameters(index = "0", description = "result file A")
    private File resultFileA;

    @CommandLine.Option(names = {"-apikey"}, description = "ApiKey", required = true)
    private String apiKey;

    @CommandLine.Option(names = {"-inFile"}, description = "file to read", required = true, defaultValue = "default_in_file")
    private File inputFile;

    @CommandLine.Option(names = {"-optionalFlag"}, description = "optional flag", required = false)
    private File optionalFlag;

    // ----

    public static void main(final String[] args)
    {
        System.exit(new CommandLine(new Runner_PicoliSampleCLI()).execute(args));
    }

    @Override public Integer call()
    {
        if (CompilerSwitches.HIDE_IMPLEMENTATION) {
            System.err.println("oh no");
            throw new IllegalStateException("do not call me in this environment");
        } else {
            System.out.println("fair enough!");
            return 0;
        }
    }
}
