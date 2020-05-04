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
        final JSONObject jo = PicoliUtils.convert(Runner_PicoliSampleCLI.class.getName(), new CommandLine(new Runner_PicoliSampleCLI()));

        final JSONArray ja = jo.getJSONArray("args");
        Assert.assertEquals(5, ja.length());

        Assert.assertEquals("-apikey", ja.getJSONObject(0).get("shortestName"));
        Assert.assertFalse(ja.getJSONObject(1).getBoolean("required"));
        Assert.assertEquals(1, ja.getJSONObject(4).getInt("index"));
    }
}
