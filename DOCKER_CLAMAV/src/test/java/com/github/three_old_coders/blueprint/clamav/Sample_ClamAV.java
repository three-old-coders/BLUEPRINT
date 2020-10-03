package com.github.three_old_coders.blueprint.clamav;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Sample_ClamAV
{
    public static void main(final String[] args) throws Exception
    {
        final ClassLoader cl = Sample_SB_ClamAv.class.getClassLoader();

        {
            final URL res = cl.getResource("Brain.png");
            final File uf = new File(res.getFile());
            System.out.println(scanStream(new FileInputStream(uf)));
        }

        {
            final URL res = cl.getResource("eicar_com.zip");
            final File uf = new File(res.getFile());
            System.out.println(scanStream(new FileInputStream(uf)));
        }
    }

    public static String scanStream(final InputStream inputStream)
    {
        try (final Socket socket = new Socket("localhost", 3310);
             final OutputStream outStream = new BufferedOutputStream(socket.getOutputStream());
             final InputStream inStream = socket.getInputStream();
             final InputStream inputStream_ = inputStream;
        ) {
            socket.setSoTimeout(2000);
            outStream.write("zINSTREAM\0".getBytes(StandardCharsets.US_ASCII));
            outStream.flush();
            final byte[] buffer = new byte[2048];
            int read = inputStream.read(buffer);
            while (read >= 0) {
                final byte[] chunkSize = ByteBuffer.allocate(4).putInt(read).array();
                outStream.write(chunkSize);
                outStream.write(buffer, 0, read);
                if (inStream.available() > 0) {
                    byte[] reply = IOUtils.toByteArray(inStream);
                    return "CHECK FAILED: " + new String(reply, StandardCharsets.US_ASCII);
                }
                read = inputStream.read(buffer);
            }
            outStream.write(new byte[]{0, 0, 0, 0});
            outStream.flush();
            final String response = StringUtils.trimToEmpty(new String(IOUtils.toByteArray(inStream)));
            if (response.startsWith("stream: ")) {
                if ("OK".equals(response.substring(8))) {
                    return null;
                } else if (response.endsWith(" FOUND")) {
                    return response.substring(8, response.length() - 6);
                }
            }
        } catch (final Exception e) {
            return "CHECK FAILED: " + e.getMessage();
        }

        return "CHECK FAILED: (unknown)";
    }
}
