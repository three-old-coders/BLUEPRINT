package com.github.three_old_coders.blueprint.rundeck;

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

    //
    // ---->> PROTECTED
    //

    protected abstract Class<?> getPicoliClass();

    @Override protected JSONObject getCLI()
    {
        final Class<?> clazz = getPicoliClass();
        final CommandLine cl = PicoliUtils.createFrom(clazz);

        return PicoliUtils.convert(clazz.getName(), cl);
    }
}
