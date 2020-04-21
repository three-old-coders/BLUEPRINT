package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;

import java.util.Map;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java
// /Library/Java/JavaVirtualMachines/jdk-11.0.3.jdk/Contents/Home/bin/java -cp ./JDK8/target/rundeck-jdk8-0.0.1-SNAPSHOT-final-shade.jar com.github.three_old_coders.blueprint.rundeck.Runner_PicoliCLI -apikey=MY_API_KEY resultFileA resultFileB

@Plugin(name = RundeckJavaPicoliWorkflowStepPlugin.PROVIDER_NAME, service = ServiceNameConstants.WorkflowStep)
public class RundeckJavaPicoliWorkflowStepPlugin
    extends RundeckJavaPicoliWorkflowStepPluginBase
{
    public static final String PROVIDER_NAME = "three-old-coders";

    public RundeckJavaPicoliWorkflowStepPlugin()
    {
        super(PROVIDER_NAME, "Java Picoli CLI Workflow Step",
            "WorkflowStep written in Java to execute Picoli CLI based Java classes");
    }

    @Override public void executeStep(final PluginStepContext pluginStepContext, final Map<String, Object> map)
        throws StepException
    {
        System.out.println(pluginStepContext);
        System.out.println(map);
        System.out.println(new Runner_PicoliCLI().call());
    }

    protected Class getPicoliClass()
    {
        return Runner_PicoliCLI.class;
    }
}
