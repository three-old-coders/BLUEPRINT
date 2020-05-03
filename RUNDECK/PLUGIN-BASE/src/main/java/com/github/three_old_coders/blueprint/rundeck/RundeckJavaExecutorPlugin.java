package com.github.three_old_coders.blueprint.rundeck;

import com.dtolabs.rundeck.core.common.Framework;
import com.dtolabs.rundeck.core.common.INodeEntry;
import com.dtolabs.rundeck.core.execution.ExecutionContext;
import com.dtolabs.rundeck.core.execution.impl.local.LocalNodeExecutor;
import com.dtolabs.rundeck.core.execution.service.NodeExecutor;
import com.dtolabs.rundeck.core.execution.service.NodeExecutorResult;
import com.dtolabs.rundeck.core.execution.service.NodeExecutorResultImpl;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;

import java.util.Arrays;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java

@Plugin(name = RundeckJavaExecutorPlugin.PROVIDER_NAME, service = ServiceNameConstants.NodeExecutor)
@PluginDescription(title = "Java CLI Node Executor", description = "NodeExecutor written in Java to execute Java 'static main' classes")
public class RundeckJavaExecutorPlugin
    implements NodeExecutor
{
    public static final String PROVIDER_NAME = "java-cli-NodeExecutor";

    private final LocalNodeExecutor _lne;

    public RundeckJavaExecutorPlugin(final Framework framework)
    {
        _lne = new LocalNodeExecutor(framework);
    }

    @Override
    public NodeExecutorResult executeCommand(final ExecutionContext context, final String[] command, final INodeEntry node)
    {
        System.out.println(context);
        System.out.println(Arrays.toString(command));
        System.out.println(node);

        return NodeExecutorResultImpl.createSuccess(node);
    }
}
