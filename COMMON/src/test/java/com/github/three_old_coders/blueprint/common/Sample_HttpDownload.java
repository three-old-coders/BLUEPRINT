package com.github.three_old_coders.blueprint.common;

import lombok.SneakyThrows;
import org.jooq.lambda.tuple.Tuple3;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * just a sample as unit testing is quite difficult using external servers and faking the core functionality makes no sense to me...
 */
public class Sample_HttpDownload
{
    @SneakyThrows public static void main(final String[] args)
    {
        final List<URL> urls = Arrays.asList(
            new URL("https://www.xetra.com/resource/blob/1524/2e259ba7cee3ee42fbd05c8ed38bbe68/data/allTradableInstruments.csv"),
            new URL("https://www.3oc.de/THIS_FILE_DOES_NOT_EXIST")
        );

        final File destinatioDir = new File("/Volumes/RAM-Disk/download-test");
        final HttpUtils.IDownloadEvents downloadEvents = new HttpUtils.IDownloadEvents()
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
        
        final List<Tuple3<URL, File, Exception>> t3s_1 = HttpUtils.parallelFileDownload(urls, true, destinatioDir, downloadEvents);
        final List<Tuple3<URL, File, Exception>> t3s_2 = HttpUtils.parallelFileDownload(urls.subList(1, 2), true, destinatioDir, null);
        final List<Tuple3<URL, File, Exception>> t3s_3 = HttpUtils.parallelFileDownload(urls.subList(0, 1), false, destinatioDir, downloadEvents);

        System.out.println("done...");
    }
}
