package com.github.three_old_coders.blueprint.rundeck;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * this base class creates rundeck plugin input fields by JSON definitions located in /META-DATA/RUNDECK/BLUEPRINT-CLI/classname.json
 */
public abstract class RundeckJavaPicoliJsonResourceWorkflowStepPluginBase
    extends RundeckJavaPicoliJsonWorkflowStepPluginBase
{
    public static final String DATA_SUB_DIR = "META-DATA/RUNDECK-PLUGIN/BLUEPRINT-CLI/";

    /**
     * ctor
     * @param providerName
     * @param title
     * @param description
     * @param jsonResourceName  (without .json)
     */
    public RundeckJavaPicoliJsonResourceWorkflowStepPluginBase(final String providerName,
                                                               final String title,
                                                               final String description,
                                                               final String jsonResourceName)
    {
        super(providerName, title, description, readJSON(jsonResourceName));
    }

    //
    // ---->> PRIVATE
    //
    
    private static JSONObject readJSON(final String jsonResourceName)
    {
        final ClassLoader cl = RundeckJavaPicoliJsonResourceWorkflowStepPluginBase.class.getClassLoader();
        // final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (final InputStream is = cl.getResourceAsStream(DATA_SUB_DIR + jsonResourceName + ".json")) {
            final List<String> strings = IOUtils.readLines(is, StandardCharsets.UTF_8);
            return new JSONObject(StringUtils.join(strings, ""));
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
