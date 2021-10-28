package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.plugins.configuration.Description;
import junit.framework.TestCase;
import org.junit.Assert;

public class TestCase_CliToDescriptionConversion
    extends TestCase
{
    public void testRundeckPicoliPlugin()
    {
        final RundeckJavaPicoliSampleWorkflowStepPlugin p = new RundeckJavaPicoliSampleWorkflowStepPlugin()
        {
            @Override protected Class getPicoliClass()
            {
                return Runner_PicoliSampleCLI_1.class;
            }
        };
        final Description description = p.getDescription();
        Assert.assertEquals(6, description.getProperties().size());
    }
}
