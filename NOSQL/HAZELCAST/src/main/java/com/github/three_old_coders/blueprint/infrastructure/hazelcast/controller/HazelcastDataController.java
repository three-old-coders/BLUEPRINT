package com.github.three_old_coders.blueprint.infrastructure.hazelcast.controller;

import com.google.common.io.CharStreams;
import com.hazelcast.core.HazelcastInstance;
import com.github.three_old_coders.blueprint.common.CompactingUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://stackoverflow.com/questions/19444855/spring-rest-post-json-requestbody-content-type-not-supported
 * https://www.n-k.de/2016/05/optional-path-variables-with-spring-boot-rest-mvc.html
 * https://my.bluehost.com/hosting/help/create-mime-types
 */
@Controller
@RestController
public class HazelcastDataController
{
    public static final String APPLICATION_JSON = "Application/json";
    public static final String APPLICATION_JSON_BASE_64_COMPRESSION_ZIP = "Application/json;compression=zip;encoding=base64";

    private static class MetaData implements Serializable
    {
        private String _mimeType;
        private String _data;

        public MetaData(final String mimeType, final String data)
        {
            _mimeType = mimeType;
            _data = data;
        }
    }

    private final HazelcastInstance _hi;

    public HazelcastDataController(final HazelcastInstance hazelcastInstance)
    {
        this._hi = hazelcastInstance;
    }

    // todo: mime type --> type/subtype;parameter=value --> Application/base64;compression=zip
    @RequestMapping(method = RequestMethod.POST, value = "/map-api/{map}/{key}")
    @ResponseBody
    public String writeDataToHazelcast(@PathVariable String map, @PathVariable String key, final HttpServletRequest request)
    {
        try (final BufferedReader br = request.getReader()) {
            final Map<String, MetaData> hazelcastMap = _hi.getMap(decode(map));
            // todo: check mime type
            final String[] mimeParamsOrig = StringUtils.split(request.getContentType(), ";");
            final String[] mimeParameters = Arrays.stream(mimeParamsOrig).map(String::trim).map(String::toLowerCase).toArray(String[]::new);
            final Set<String> mps = new HashSet<>(Arrays.asList(mimeParameters));
            if (mps.contains("application/json") && mps.contains("compression=zip") && mps.contains("encoding=base64")) {
                final MetaData metaData = new MetaData(APPLICATION_JSON_BASE_64_COMPRESSION_ZIP, CharStreams.toString(br));
                hazelcastMap.put(decode(key), metaData);
                return "ok\n";
            }

            throw new IllegalArgumentException("unsupported mime type [" + request.getContentType() + "], use [" + APPLICATION_JSON_BASE_64_COMPRESSION_ZIP + "]");
        } catch (final Exception e) {
            throw new IllegalArgumentException("invalid post data", e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/map-api/{map}/{key}/{value}")
    @ResponseBody
    public String writeDataToHazelcast(@PathVariable String map, @PathVariable String key, @PathVariable String value)
    {
        final Map<String, MetaData> hazelcastMap = _hi.getMap(decode(map));
        final MetaData metaData = new MetaData(APPLICATION_JSON, decode(value));
        hazelcastMap.put(decode(key), metaData);
        return "ok\n";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/map-api/{map}/{key}")
    @ResponseBody
    public String readDataFromHazelcast(@PathVariable String map, @PathVariable String key)
    {
        final Map<String, MetaData> hazelcastMap = _hi.getMap(decode(map));
        final MetaData metaData = hazelcastMap.get(decode(key));
        if (null != metaData) {
            if (APPLICATION_JSON.equals(metaData._mimeType)) {
                return metaData._data;
            } else if (APPLICATION_JSON_BASE_64_COMPRESSION_ZIP.equals(metaData._mimeType)) {
                return CompactingUtils.decodeBase64AndDecompress(metaData._data);
            }
        }

        return null;
    }

//    @GetMapping(value = "/delete/{key}")
//    public String removeDataFromHazelcast(@PathVariable String key)
//    {
//        Map<String, String> hazelcastMap = hazelcastInstance.getMap("my-map");
//        return hazelcastMap.remove(key);
//    }
//
//    @GetMapping(value = "/read-all")
//    public Map<String, String> readAllDataFromHazelcast()
//    {
//        return hazelcastInstance.getMap("my-map");
//    }

    //
    // ---->> PRIVATE
    //

    private String decode(final String value)
    {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
