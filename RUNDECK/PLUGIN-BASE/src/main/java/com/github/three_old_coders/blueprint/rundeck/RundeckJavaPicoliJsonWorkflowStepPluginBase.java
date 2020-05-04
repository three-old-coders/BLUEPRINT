package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.execution.service.ProviderLoaderException;
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.execution.workflow.steps.StepFailureReason;
import com.dtolabs.rundeck.core.plugins.PluginMetadata;
import com.dtolabs.rundeck.core.plugins.ServiceProviderLoader;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.core.plugins.configuration.Property;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;
import com.dtolabs.rundeck.plugins.step.StepPlugin;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
import io.github.classgraph.*;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.json.JSONObject;
import org.zeroturnaround.exec.ProcessExecutor;
import picocli.CommandLine;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java

/**
 * this base class creates rundeck plugin input fields by JSON definitions located in /META-INF/RUNDECK/BLUEPRINT-CLI/classname.json
 */
public abstract class RundeckJavaPicoliJsonWorkflowStepPluginBase
    extends RundeckJavaPicoliWorkflowStepPluginBase
{
    public RundeckJavaPicoliJsonWorkflowStepPluginBase(final String providerName, final String title, final String description)
    {
        super(providerName, title, description);
    }

    //
    // ---->> PROTECTED
    //
    
    @Override protected JSONObject getCLI()
    {
        return null;
    }
}
