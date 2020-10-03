package com.github.three_old_coders.blueprint.clamav;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Sample_SB_ClamAv
{
    public static void main(final String[] args)
    {
        // final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        final ClassLoader cl = Sample_SB_ClamAv.class.getClassLoader();

        final URL resource1 = cl.getResource("Brain.png");
        final File uploadFile1 = new File(resource1.getFile());
        final URL resource2 = cl.getResource("eicar_com.zip");
        final File uploadFile2 = new File(resource2.getFile());

        try (
            final InputStream is1 = resource1.openStream();
            final InputStream is2 = resource2.openStream();
        ) {
            final HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("files", is1, ContentType.IMAGE_PNG, uploadFile1.getName())
                .addBinaryBody("files", is2, ContentType.WILDCARD, uploadFile2.getName())
                .setCharset(StandardCharsets.US_ASCII).build();

            final HttpPost httpPost = new HttpPost("http://localhost:3316/scan");
            httpPost.setEntity(httpEntity);

            final HttpClient httpClient = new DefaultHttpClient();
            final HttpResponse httpResponse = httpClient.execute(httpPost);

            final HttpEntity httpEntityResult = httpResponse.getEntity();
            final ByteArrayOutputStream resultAsStream = new ByteArrayOutputStream();
            IOUtils.copy(httpEntityResult.getContent(), resultAsStream);

            System.out.println(resultAsStream.toString());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
