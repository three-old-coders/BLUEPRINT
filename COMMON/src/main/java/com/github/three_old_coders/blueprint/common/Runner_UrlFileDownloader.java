package com.github.three_old_coders.blueprint.common;

import org.jooq.lambda.tuple.Tuple3;
import picocli.CommandLine;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(
        description = "Downloads arbitrary files for given URLs", name = "Runner_UrlFileDownloader",
        mixinStandardHelpOptions = true, version = "0.1"
)
public class Runner_UrlFileDownloader
    implements Callable<Integer>
{
    @CommandLine.Option(names = {"-destinationDir"}, description = "Destination dir", required = true)
    private File destinationDir;

    @CommandLine.Option(names = {"-keepSubdirectories"}, description = "Keep subdirectory structure from URL (default false)", defaultValue = "false")
    private boolean keepSubdirectories;

    @CommandLine.Option(names = {"-url1"}, description = "URL 1", required = true)
    private URL url1;

    @CommandLine.Option(names = {"-url2"}, description = "URL 2")
    private URL url2;

    @CommandLine.Option(names = {"-url3"}, description = "URL 3")
    private URL url3;

    @CommandLine.Option(names = {"-url4"}, description = "URL 4")
    private URL url4;

    @CommandLine.Option(names = {"-url5"}, description = "URL 5")
    private URL url5;

    private final HttpUtils.IDownloadEvents _downloadEvents = new HttpUtils.IDownloadEvents()
    {
        @Override public void started(final URL url)
        {
            System.out.println("downloading [" + url + "]");
        }

        @Override public void completed(final Tuple3<URL, File, Exception> tuple3)
        {
            if (null == tuple3.v3) {
                System.out.println("downloaded [" + tuple3.v1 + "] to [" + tuple3.v2 + "]");
            } else {
                System.out.println("failed to download [" + tuple3.v1 + "] because of [" + tuple3.v3 + "]");
            }
        }
    };

    // ----

    public static void main(final String[] args)
    {
        System.exit(new CommandLine(new Runner_UrlFileDownloader()).execute(args));
    }

    //
    // ---->> PUBLIC
    //

    @Override
    public Integer call()
    {
        final List<URL> urls = new ArrayList<>();
        add(urls, url1);
        add(urls, url2);
        add(urls, url3);
        add(urls, url4);
        add(urls, url5);

        final List<Tuple3<URL, File, Exception>> tuple3s = HttpUtils.parallelFileDownload(urls, keepSubdirectories, destinationDir, _downloadEvents);
        for (final Tuple3<URL, File, Exception> tuple3 : tuple3s) {
            if (null != tuple3.v3) {
                return 1;
            }
        }

        return 0;
    }

    //
    // ---->> PRIVATE
    //

    private void add(final List<URL> urls, final URL url)
    {
        if (null != url) {
            urls.add(url);
        }
    }
}
