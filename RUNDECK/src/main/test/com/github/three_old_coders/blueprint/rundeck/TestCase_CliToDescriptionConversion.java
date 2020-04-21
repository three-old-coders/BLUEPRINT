package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.plugins.configuration.Description;
import junit.framework.TestCase;
import org.junit.Assert;

public class TestCase_CliToDescriptionConversion
    extends TestCase
{
    public void testRundeckPicoliPlugin()
    {
        final RundeckJavaPicoliWorkflowStepPlugin p = new RundeckJavaPicoliWorkflowStepPlugin()
        {
            @Override protected Class getPicoliClass()
            {
                return Runner_PicoliCLI.class;
            }
        };

        final Description description = p.getDescription();
        System.out.println(description.getProperties().toString());

        Assert.assertEquals(6, description.getProperties().size());
    }
}
