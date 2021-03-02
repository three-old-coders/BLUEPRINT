package com.github.three_old_coders.blueprint.clamav.controller;

import fi.solita.clamav.ClamAVClient;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class ClamAvScanController
{
    public static final String STATUS = "status";
    public static final String STATUS_CHECKED = "checked";
    public static final String STATUS_DANGER = "DANGER";
    public static final String FILE = "file";
    public static final String INFO = "info";

    @Configuration
    @ConfigurationProperties(prefix = "clamav")
    @Data
    public static class ClamAvConfigProperties
    {
        private String host;
        private int port;
    }

    private final ClamAVClient _cac;

    public ClamAvScanController(final ClamAvConfigProperties clamAvConfigProperties)
    {
        _cac = new ClamAVClient(clamAvConfigProperties.getHost(), clamAvConfigProperties.getPort());
    }

    // ----

    @RequestMapping("/")
    public String root()
    {
        return "redirect:/status";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getStatus()
    {
        try {
            return new JSONObject(STATUS, _cac.ping() ? "ok" : "ERR").toCompactString();
        } catch (final Exception e) {
            return new JSONObject(STATUS, "ERR", INFO, e.getMessage()).toCompactString();
        }
    }

    @PostMapping(value = "/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String doScan(@RequestParam(value = "files", required = false) final List<MultipartFile> files)
    {
        boolean virusFound = false;
        final JSONArray jaScanResults = new JSONArray();
        for (final MultipartFile file : files) {
            final String fn = file.getOriginalFilename();
            try {
                final String strResult = scanStream(file.getInputStream());
                if (null == strResult) {
                    jaScanResults.put(new JSONObject(FILE, fn, STATUS, STATUS_CHECKED));
                } else {
                    jaScanResults.put(new JSONObject(FILE, fn, STATUS, STATUS_DANGER, "virus", strResult));
                    virusFound = true;
                }
            } catch (final Exception e) {
                jaScanResults.put(new JSONObject(FILE, fn, STATUS, STATUS_DANGER, INFO, e.getMessage()));
            }
        }

        return new JSONObject(STATUS, virusFound ? STATUS_DANGER : STATUS_CHECKED,
                              "scanResults", jaScanResults).toCompactString();
    }

    //
    // ----->> PRIVATE
    //

    private String scanStream(final InputStream inputStream) throws IOException
    {
        try (final InputStream inputStream_ = inputStream) {
            final byte[] scanResult = _cac.scan(inputStream_);
            if (!ClamAVClient.isCleanReply(scanResult)) {
                final String response = StringUtils.trimToEmpty(new String(scanResult, StandardCharsets.US_ASCII));
                return response.substring(8, response.length() - 6);
            } else {
                return null;
            }
        }
    }
}
