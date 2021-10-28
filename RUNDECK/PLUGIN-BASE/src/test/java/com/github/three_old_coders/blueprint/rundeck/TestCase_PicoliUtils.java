package com.github.three_old_coders.blueprint.rundeck;

import junit.framework.TestCase;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.junit.Assert;
import picocli.CommandLine;

public class TestCase_PicoliUtils
    extends TestCase
{
    public void testConversion()
    {
        final JSONObject jo = PicoliUtils.convert(Runner_PicoSampleCLI_2.class.getName(), new CommandLine(new Runner_PicoSampleCLI_2()));

        final JSONArray ja = jo.getJSONArray("args");
        System.out.println(ja);
        Assert.assertEquals(7, ja.length());

        Assert.assertEquals("-apikey", ja.getJSONObject(1).get("shortestName"));
        Assert.assertFalse(ja.getJSONObject(2).getBoolean("required"));
        Assert.assertEquals(1, ja.getJSONObject(6).getInt("index"));

        try {
            Runner_PicoSampleCLI_2.main(new String[]{"-Da", "-Db=c", "-apikey APIKEY"});
        } catch (final Exception e) {
            Assert.assertTrue(e.getMessage().contains("Missing required parameters: '<resultFileA>', '<resultFileB>'"));
        }
    }
}
