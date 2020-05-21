package com.github.three_old_coders.blueprint.common;

import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLUtils
{
    private URLUtils()
    {
    }

    @SneakyThrows public static String encode(final String s)
    {
        return URLEncoder.encode(s, StandardCharsets.UTF_8.name());
    }
}
