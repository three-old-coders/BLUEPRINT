package com.github.three_old_coders.blueprint.rundeck;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java

import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import org.apache.tapestry5.json.JSONObject;

/**
 * this base class creates rundeck plugin input fields by JSON definitions located in /META-INF/RUNDECK/BLUEPRINT-CLI/classname.json
 */
public abstract class RundeckJavaPicoliJsonWorkflowStepPluginBase
    extends RundeckJavaPicoliWorkflowStepPluginBase
{
    private final JSONObject _jo;

    public RundeckJavaPicoliJsonWorkflowStepPluginBase(final String providerName, final String title, final String description, final JSONObject jo)
    {
        super(providerName, title, description);

        _jo = jo;
    }

    public Description getDescription()
    {
        final DescriptionBuilder descriptionBuilder = createDescriptionBuilder();
        DescriptionUtils.addProperties(descriptionBuilder, _jo);
        return descriptionBuilder.build();
    }

    //
    // ---->> PROTECTED
    //

    @Override protected String getExecutionClass()
    {
        return _jo.getString("id");
    }
}
