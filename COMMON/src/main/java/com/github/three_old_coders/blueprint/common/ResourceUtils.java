package com.github.three_old_coders.blueprint.common;

import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class ResourceUtils
{
    /**
     * retrieves a sysprop or env value for key.
     * <ul>
     * <li>if key is undefined null is returned</li>
     * </ul>
     *
     * @param key
     * @return value
     */
    public static String getSysPropEnv(final String key)
    {
        final String value = System.getProperty(key);
        return (value!=null)?value:System.getenv(key);
    }

    /**
     * retrieve an environment variable first and if the environment variable does not exist try the key as a system property
     *
     * @param key
     * @return environment variable value or system property value or null, if key is undefined
     */
    public static String getEnvSysProp(final String key)
    {
        final String value = System.getenv(key);
        return (value!=null)?value:System.getProperty(key);
    }

    /**
     * retrieve an environment variable first and if the environment variable does not exist try the key as a system property
     *
     * @param key
     * @return environment variable value or system property value or defaultValue, if key is undefined
     */
    public static String getEnvSysProp(final String key,final String defaultValue)
    {
        final String value = getEnvSysProp(key);
        return (value!=null)?value:defaultValue;
    }


    public static URL getParent(final URL url, final boolean keepDirSeparator)
        throws MalformedURLException
    {
        final String ef = url.toExternalForm().replace('\\', '/');
        int pos = ef.lastIndexOf('/');
        if (pos > 0) {
            return new URL(ef.substring(0, pos + (keepDirSeparator ? 1 : 0)));
        }

        return url;
    }

    public static String getName(final URL url)
        throws MalformedURLException
    {
        final String ef = url.toExternalForm().replace('\\', '/');
        int pos = ef.lastIndexOf('/');
        if (pos > 0) {
            return ef.substring(pos + 1);
        }

        return url.getPath();
    }

    // http://stackoverflow.com/questions/861500/url-to-load-resources-from-the-classpath-in-java
    public static URL getResource(final String location)
    {
        return getResource(location,Thread.currentThread().getContextClassLoader());
    }

    public static URL getResource(final String location, final ClassLoader cl)
    {
        final String protocol = location.replaceFirst("^(\\w+):.+$", "$1").toLowerCase();
        switch(protocol) {
            case "http":
            case "https":
            case "file":
                return createURL(location);
            case "classpath":
            case "cp":
                return cl.getResource(location.replaceFirst("^\\w+:", ""));
            default:
                try {
                    final URL url = cl.getResource(location);
                    return (url!=null)?url:new File(location).toURI().toURL();
                } catch (final MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
        }
    }

    @SneakyThrows public static Properties loadProperties(final URL url)
    {
        final Properties p = new Properties();

        try (final InputStream is = url.openStream()) {
            p.load(is);
        }

        return p;
    }

    //
    // ---->> PRIVATE
    //

    private static URL createURL(final String value)
    {
        try {
            return new URL(value);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
