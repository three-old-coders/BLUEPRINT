package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.plugins.configuration.Description;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.junit.Assert;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestCase_CliToDescriptionConversion
    extends TestCase
{
    public void testRundeckPicoliPlugin()
    {
        final RundeckJavaPicoliSampleWorkflowStepPlugin p = new RundeckJavaPicoliSampleWorkflowStepPlugin()
        {
            @Override protected Class getPicoliClass()
            {
                return Runner_PicoliSampleCLI.class;
            }
        };
        final Description description = p.getDescription();
        Assert.assertEquals(6, description.getProperties().size());
    }
}
