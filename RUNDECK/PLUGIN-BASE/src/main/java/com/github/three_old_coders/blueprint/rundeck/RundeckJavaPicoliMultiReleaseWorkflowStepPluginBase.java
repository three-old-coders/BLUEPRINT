package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import org.apache.tapestry5.json.JSONObject;
import picocli.CommandLine;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java

public abstract class RundeckJavaPicoliMultiReleaseWorkflowStepPluginBase
    extends RundeckJavaPicoliWorkflowStepPluginBase
{
    public RundeckJavaPicoliMultiReleaseWorkflowStepPluginBase(final String providerName, final String title, final String description)
    {
        super(providerName, title, description);
    }

    @Override public Description getDescription()
    {
        final Class<?> clazz = getPicoliClass();
        final CommandLine cl = PicoliUtils.createFrom(clazz);
        final JSONObject jo = PicoliUtils.convert(clazz.getName(), cl);
        final DescriptionBuilder descriptionBuilder = createDescriptionBuilder();
        DescriptionUtils.addProperties(descriptionBuilder, jo);
        return descriptionBuilder.build();
    }

    //
    // ---->> PROTECTED
    //

    protected abstract Class<?> getPicoliClass();

    @Override protected String getExecutionClass()
    {
        return getPicoliClass().getName();
    }
}
