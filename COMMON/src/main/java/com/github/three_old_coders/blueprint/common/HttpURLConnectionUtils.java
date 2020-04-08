package com.github.three_old_coders.blueprint.common;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpURLConnectionUtils
{
    public static void retrieveResponse(final HttpURLConnection uc, final Optional<InputStream> requestData, final OutputStreamWriter osw)
        throws Exception
    {
        if (!requestData.isEmpty()) {
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
}
