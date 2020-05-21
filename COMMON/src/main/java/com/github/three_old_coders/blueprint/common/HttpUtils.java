package com.github.three_old_coders.blueprint.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jooq.lambda.tuple.Tuple3;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class HttpUtils
{
    public static void retrieveResponse(final HttpURLConnection uc, final Optional<InputStream> requestData, final OutputStreamWriter osw)
        throws Exception
    {
        if (requestData.isPresent()) {
            try (final OutputStream os = new DataOutputStream(uc.getOutputStream())) {
                IOUtils.copy(requestData.get(), os);
            }
        }

        try(final BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8.name()))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                osw.write(responseLine);
            }

            osw.flush();
        } finally {
            osw.close();
            uc.disconnect();
        }
    }

    public interface IDownloadEvents
    {
        void started(final URL url);
        void completed(final Tuple3<URL, File, Exception> tuple3);
    }

    /**
     * downloads files in parallel using http
     * @param downloadUrls
     * @param keepUrlSubdirectories
     * @param destinatioDir
     * @param downloadEvents optional
     * @return
     */
    public static List<Tuple3<URL, File, Exception>> parallelFileDownload(
            final List<URL> downloadUrls,
            final boolean keepUrlSubdirectories,
            final File destinatioDir,
            final IDownloadEvents downloadEvents)
    {
        destinatioDir.mkdirs();
        
        final List<Tuple3<URL, File, Exception>> downloads = new ArrayList<>();
        final BlockingExecutorService bes = new BlockingExecutorService(10, 100, 60*5);
        for (final URL downloadUrl : downloadUrls) {
            bes.execute(() -> {
                Tuple3<URL, File, Exception> t3;
                try {
                    executeCatchAll(() -> downloadEvents.started(downloadUrl));

                    final Path path = Paths.get(downloadUrl.getPath());
                    final String[] pathElements = StreamSupport.stream(path.spliterator(), false).map(Path::toString).toArray(String[]::new);
                    File d = destinatioDir;
                    if (keepUrlSubdirectories) {
                        for (int i = 0; i < pathElements.length - 1; i++) {
                            final String pathElement = pathElements[i];
                            d = new File(d, pathElement);
                        }
                    }

                    final File f = new File(d, pathElements[pathElements.length - 1]);
                    try {
                        d.mkdirs();
                        FileUtils.copyURLToFile(downloadUrl, f);
                        t3 = new Tuple3<>(downloadUrl, f, null);
                    } catch (final Exception e) {
                        t3 = new Tuple3<>(downloadUrl, f, e);
                    }
                } catch (Exception e) {
                    t3 = new Tuple3<>(downloadUrl, null, e);
                }

                downloads.add(t3);

                final Tuple3<URL, File, Exception> t3f = t3;
                executeCatchAll(() -> downloadEvents.completed(t3f));
            });
        }

        bes.shutdownAndAwaitTermination();
        
        return downloads;
    }

    public static void executeCatchAll(final Runnable r)
    {
        try {
            r.run();
        } catch (final Exception e) {
        }
    }
}
