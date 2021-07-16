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
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.File;
import java.util.*;

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

            final Description description = getDescription();
            final SortedMap<Integer, String> orderedArgs = new TreeMap<>();
            List<String> jvmArgs = null;
            List<String> programParams = new ArrayList<>();
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                if (null != value) {
                    final String argKey = entry.getKey();
                    String argValue = StringUtils.trimToEmpty(value.toString());
                    // be careful: logs passwords System.out.println("argKey: " + argKey + ", argValue: " + argValue);
                    // handle unnamed arguments (keep order 0..n)
                    if (argKey.startsWith("arg ")) {
                        final String[] keyParts = StringUtils.split(argKey, " ");
                        final String orderNumber = keyParts[1];
                        orderedArgs.put(Integer.parseInt(orderNumber), argValue);
                    } else if (!JDK.equals(argKey)) {
                        // {JDK=jdk-11.0.7, inputFile=default_in_file, arg 1 resultFileB=B, apiKey=12345, arg 0 resultFileA=A}
                        // convert to
                        // java -cp JDK8/target/rundeck-jdk8-0.0.1-SNAPSHOT.jar com.github.three_old_coders.blueprint.rundeck.Runner_PicoliCLI -inFile=default_in_file -apikey=12345 A B
                        if ("jvmSettings".equals(argKey)) {
                            if (argValue.startsWith("\"") && argValue.endsWith("\"")) {
                                argValue = argValue.substring(1, argValue.length()-1);
                            }
                            jvmArgs = Arrays.asList(StringUtils.split(argValue, " "));
                        } else {
                            for (final Property property : description.getProperties()) {
                                if (argKey.equals(property.getName())) {
                                    if ("Boolean".equals(property.getType().name())) {
                                        if ("true".equals(argValue)) {
                                            programParams.add(property.getTitle());
                                        }
                                    } else {
                                        programParams.add(property.getTitle());
                                        programParams.add(argValue);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (null != jvmArgs) {
                scriptArgs.addAll(jvmArgs);
            }

            scriptArgs.add(getExecutionClass());
            scriptArgs.addAll(programParams);
            for (final Map.Entry<Integer, String> integerStringEntry : orderedArgs.entrySet()) {
                scriptArgs.add(integerStringEntry.getValue());
            }

            // be careful: logs passwords System.out.println("argKey: " + argKey + ", argValue: " + argValue);
            // System.out.println(scriptArgs);

            final int exitCode = new ProcessExecutor().command(scriptArgs).redirectOutput(System.out).execute().getExitValue();
            if (0 != exitCode) {
                throw new StepException("external JVM process returned error [" + exitCode + "]", StepFailureReason.PluginFailed);
            }

        } catch (final Exception e) {
            throw new StepException("failed to execute external JVM process", e, StepFailureReason.PluginFailed);
        }
    }

    //
    //  ---->> PROTECTED
    //

    protected abstract String getExecutionClass();

    protected DescriptionBuilder createDescriptionBuilder()
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

        return descriptionBuilder;
    }
}
