package com.github.three_old_coders.blueprint.rundeck;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java

/**
 * this base class creates rundeck plugin input fields by JSON definitions located in /META-INF/RUNDECK/BLUEPRINT-CLI/classname.json
 */
public abstract class RundeckJavaPicoliJsonResourceWorkflowStepPluginBase
    extends RundeckJavaPicoliJsonWorkflowStepPluginBase
{
    private final String jsonResourceName;

    public RundeckJavaPicoliJsonResourceWorkflowStepPluginBase(final String providerName,
                                                               final String title,
                                                               final String description,
                                                               final String jsonResourceName)
    {
        super(providerName, title, description);
        this.jsonResourceName = jsonResourceName;
    }

    //
    // ---->> PROTECTED
    //
    
    @Override protected JSONObject getCLI()
    {
        final ClassLoader cl = this.getClass().getClassLoader();
        try (final InputStream is = cl.getResourceAsStream("META-DATA/RUNDECK-PLUGIN/BLUEPRINT-CLI/" + jsonResourceName)) {
            final List<String> strings = IOUtils.readLines(is, StandardCharsets.UTF_8);
            return new JSONObject(StringUtils.join(strings, ""));
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
