package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import junit.framework.TestCase;
import org.apache.tapestry5.json.JSONObject;
import org.junit.Assert;
import picocli.CommandLine;

public class TestCase_DescriptionBuilder
    extends TestCase
{
    public void test()
    {
        final DescriptionBuilder descriptionBuilder = DescriptionBuilder.builder().name("providerName").title("title").description("description");
        final CommandLine commandLine = PicoliUtils.createFrom(Runner_PicoSampleCLI_2.class);
        final JSONObject jo = PicoliUtils.convert(Runner_PicoSampleCLI_2.class.getName(), commandLine);

        DescriptionUtils.addProperties(descriptionBuilder, jo);

        final Description description = descriptionBuilder.build();
        System.out.println(description);

        Assert.assertEquals(6, description.getProperties().size());
        // todo: add more tests
    }
}
