package com.github.three_old_coders.blueprint.common;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompactingUtils
{
    private CompactingUtils()
    {
    }

    /**
     * in --> zip --> base64 --> out
     *
     * @param s
     * @return
     */
    public static String compressAndEncodeBase64(final String s)
    {
        try {
            final Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(s.getBytes(StandardCharsets.UTF_8));

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            deflater.finish();
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                baos.write(buffer, 0, count);
            }
            baos.close();

            final Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(baos.toByteArray());
        } catch (final Exception e) {
            throw new IllegalStateException("unable to compact data", e);
        }
    }

    /**
     * in --> un-base64 --> un-zip --> out
     *
     * @param s
     * @return
     */
    public static String decodeBase64AndDecompress(final String s)
    {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();

            final Inflater inflater = new Inflater();
            inflater.setInput(decoder.decode(s));

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                baos.write(buffer, 0, count);
            }
            baos.close();

            return baos.toString(StandardCharsets.UTF_8);
        } catch (final Exception e) {
            throw new IllegalStateException("unable to compact data", e);
        }
    }
}
