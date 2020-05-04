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
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.File;
import java.util.*;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java

public abstract class RundeckJavaPicoliWorkflowStepPluginBase
    implements StepPlugin, Describable
{
    public static final String RUNDECK_JDK = "Rundeck JDK";
    public static final String JDK = "JDK";
    public static final String HOME_RUNDECK_ETC = "/home/rundeck/etc";

    protected final String _providerName;
    protected final String _title;
    protected final String _description;

    public RundeckJavaPicoliWorkflowStepPluginBase(final String providerName, final String title, final String description)
    {
        _providerName = providerName;
        _title = title;
        _description = description;
    }

    public Description getDescription()
    {
        return createDescription(getCLI());
    }

    @Override public void executeStep(final PluginStepContext pluginStepContext, final Map<String, Object> map)
        throws StepException
    {
        try {
            // {JDK=jdk-11.0.7, -inputFile=default_in_file, arg 1 resultFileB=B, -apiKey=12345, arg 0 resultFileA=A}

            final List<String> scriptArgs = new ArrayList<>();

            final String jdk = (String) map.get(JDK);
            if (RUNDECK_JDK.equals(jdk)) {
                scriptArgs.add("java");
            } else {
                final File[] etcJdks = new File(HOME_RUNDECK_ETC).listFiles();
                if (null != etcJdks) {
                    for (final File etcJdk : etcJdks) {
                        if (etcJdk.getName().equals(jdk)) {
                            scriptArgs.add(new File(new File(etcJdk, "bin"), "java").getAbsolutePath());
                        }
                    }
                }
            }

            if (scriptArgs.size() == 0) {
                throw new IllegalStateException("no JDK found");
            }

            try {
                final ServiceProviderLoader pm = pluginStepContext.getFramework().getPluginManager();
                final PluginMetadata pmd = pm.getPluginMetadata(ServiceNameConstants.WorkflowStep, _providerName);

                // -cp ./JDK8/target/rundeck-jdk8-0.0.1-SNAPSHOT-final-shade.jar
                scriptArgs.add("-cp");
                scriptArgs.add(pmd.getFile().getAbsolutePath());
            } catch (final ProviderLoaderException e) {
                throw new StepException("unable to detect plugin location", e, StepFailureReason.PluginFailed);
            }

            // com.github.three_old_coders.blueprint.rundeck.Runner_PicoliCLI
            final JSONObject joCLI = getCLI();
            scriptArgs.add(joCLI.getString("id"));

            final Description description = getDescription();
            final SortedMap<Integer, String> args = new TreeMap<>();
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                if (null != value) {
                    final String key = entry.getKey();
                    if (key.startsWith("arg ")) {
                        final String[] keyParts = StringUtils.split(key, " ");
                        args.put(Integer.parseInt(keyParts[1]), value.toString());
                    } else if (!JDK.equals(key)) {
                        // {JDK=jdk-11.0.7, inputFile=default_in_file, arg 1 resultFileB=B, apiKey=12345, arg 0 resultFileA=A}
                        // convert to
                        // java -cp JDK8/target/rundeck-jdk8-0.0.1-SNAPSHOT.jar com.github.three_old_coders.blueprint.rundeck.Runner_PicoliCLI -inFile=default_in_file -apikey=12345 A B

                        for (final Property property : description.getProperties()) {
                            if (key.equals(property.getName())) {
                                scriptArgs.add(property.getTitle());
                                scriptArgs.add(value.toString());
                                break;
                            }
                        }
                    }
                }
            }

            for (final Map.Entry<Integer, String> integerStringEntry : args.entrySet()) {
                scriptArgs.add(integerStringEntry.getValue());
            }

            System.out.println(scriptArgs);

            // final ProcessBuilder builder = new ProcessBuilder(scriptArgs);
            // builder.redirectError(ProcessBuilder.Redirect.INHERIT);
            // builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            // final int exitCode = builder.start().waitFor();

            final int exitCode = new ProcessExecutor().command(scriptArgs).redirectOutput(System.out).execute().getExitValue();
            if (0 != exitCode) {
                throw new StepException("external JVM process returned error [" + exitCode + "]", StepFailureReason.PluginFailed);
            }

        } catch (final Exception e) {
            throw new StepException("failed to execute external JVM process", e, StepFailureReason.PluginFailed);
        }
    }

    //
    // ---->> PROTECTED
    //

    protected abstract JSONObject getCLI();

    protected Description createDescription(final JSONObject jo)
    {
        final DescriptionBuilder descriptionBuilder = DescriptionBuilder.builder().name(_providerName).title(_title).description(_description);

        final List<String> jdks = new ArrayList<>();
        jdks.add(RUNDECK_JDK);

        final File[] etcJdks = new File(HOME_RUNDECK_ETC).listFiles();
        if (null != etcJdks) {
            for (final File etcJdk : etcJdks) {
                final String name = etcJdk.getName();
                if (name.startsWith("jdk") && !name.endsWith(".tar.gz")) {
                    jdks.add(name);
                }
            }
        }

        descriptionBuilder.property(
            PropertyBuilder.builder().select(JDK).title("Java Runtime")
                    .description("choose Java Runtime").required(true).values(jdks).build()
        ).build();

        // ----
        
        final JSONArray args = jo.getJSONArray("args");
        for (int i=0; i < args.length(); i++) {
            final JSONObject joi = args.getJSONObject(i);
            if (joi.has("isOption")) {
                descriptionBuilder.property(
                    PropertyBuilder.builder().title(joi.getString("shortestName"))
                        .string(joi.getString("label").replace("<", "").replace(">", ""))
                        .description(joi.getString("descriptions"))
                        .defaultValue(joi.has("defaultValue") ? joi.getString("defaultValue") : null)
                        .required(joi.getBoolean("required"))
                    .build()
                );
            } else if (joi.has("isPositional")) {
                descriptionBuilder.property(
                    PropertyBuilder.builder().title(joi.getString("label"))
                        .string("arg " + joi.getInt("index") + " " + joi.getString("label").replace("<", "").replace(">", ""))
                        .description(joi.getString("descriptions"))
                        .defaultValue(joi.has("defaultValue") ? joi.getString("defaultValue") : null)
                        .required(joi.getBoolean("required"))
                    .build()
                );
            }
        }

        return descriptionBuilder.build();
    }
}
