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
import org.zeroturnaround.exec.ProcessExecutor;
import picocli.CommandLine;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

// see: https://github.com/rundeck/rundeck/blob/master/examples/example-java-nodeexecutor-plugin/src/main/java/org/rundeck/plugin/example/ExampleNodeExecutorPlugin.java

public abstract class RundeckJavaPicoliWorkflowStepPluginBase
    implements StepPlugin, Describable
{
    public static final String RUNDECK_JDK = "Rundeck JDK";
    public static final String JDK = "JDK";
    public static final String HOME_RUNDECK_ETC = "/home/rundeck/etc";

    private final String _providerName;
    private final String _title;
    private final String _description;

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

            final String jdk = (String)map.get(JDK);
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
            scriptArgs.add(getPicoliClass().getName());

            final Description description = getDescription();
            final SortedMap<Integer, String> args = new TreeMap<>();
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                if (null != value) {
                    final String key = entry.getKey();
                    if (key.startsWith("arg ")) {
                        final String[] keyParts = StringUtils.split(key, " ");
                        args.put(Integer.parseInt(keyParts[1]), value.toString());
                    } else if (! JDK.equals(key)) {
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

    protected abstract Class getPicoliClass();

    public Description getDescription()
    {
        final DescriptionBuilder descriptionBuilder = DescriptionBuilder.builder().name(_providerName).title(_title).description(_description);
        final Class pc = getPicoliClass();

        try (final ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistClasses(pc.getName()).scan()) {
            for (final ClassInfo ci : scanResult.getAllClasses()) {
                final String className = ci.getName();
                final MethodInfoList miMain = ci.getMethodInfo("main");
                if (miMain.size() > 0) {
                    final AnnotationInfo ai = ci.getAnnotationInfo("picocli.CommandLine$Command");
                    if (null != ai) {
                        try {
                            final Class<?> aClass = pc.getClassLoader().loadClass(className);
                            final Object object = aClass.getDeclaredConstructor().newInstance();
                            final CommandLine cl = new CommandLine(object);

                            final List<CommandLine.Model.ArgSpec> args = cl.getCommandSpec().args();
                            final List<CommandLine.Model.PositionalParamSpec> params = new ArrayList<>();
                            final List<CommandLine.Model.OptionSpec> options = new ArrayList<>();
                            for (final CommandLine.Model.ArgSpec a : args) {
                                if (a instanceof CommandLine.Model.PositionalParamSpec) {
                                    params.add((CommandLine.Model.PositionalParamSpec)a);
                                } else if (a instanceof CommandLine.Model.OptionSpec) {
                                    if (! ((Field)a.userObject()).getDeclaringClass().getName().endsWith("CommandLine$AutoHelpMixin")) {
                                        options.add((CommandLine.Model.OptionSpec)a);
                                    }
                                }
                            }

                            Collections.sort(params, new Comparator<CommandLine.Model.PositionalParamSpec>()
                            {
                                @Override public int compare(final CommandLine.Model.PositionalParamSpec o1, final CommandLine.Model.PositionalParamSpec o2)
                                {
                                    return o1.index().compareTo(o2.index());
                                }
                            });

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

                            for (final CommandLine.Model.OptionSpec o : options) {
                                descriptionBuilder.property(
                                    PropertyBuilder.builder().title(o.shortestName())
                                        .string(o.paramLabel().replace("<", "").replace(">", ""))
                                        .description(StringUtils.join(o.description()))
                                        .defaultValue(o.defaultValue()).required(o.required()).build()
                                );
                            }

                            for (final CommandLine.Model.PositionalParamSpec p : params) {
                                descriptionBuilder.property(
                                    PropertyBuilder.builder().title(p.paramLabel())
                                        .string("arg " + p.index() + " " + p.paramLabel().replace("<", "").replace(">", ""))
                                        .description(StringUtils.join(p.description()))
                                        .defaultValue(p.defaultValue()).required(p.required()).build()
                                );
                            }
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return descriptionBuilder.build();
    }

    //
    // ---->> PRIVATE
    //


}

/*
System.out.println(a + " / " + a.required() + " / " + a.defaultValue() + " / " + a.isOption() + " / "
                    + a.isPositional() + " / " + a.paramLabel() + " / " + StringUtils.join(a.description()) + " / " + a.type());

    .property(PropertyBuilder.builder().string("bunny").title("Bunny").description("Bunny name").required(true).build())
.property(PropertyBuilder.builder().booleanType("lampkin").title("Lampkin").description("Want Lampkin?").required(false).defaultValue("false").build())
.property(PropertyBuilder.builder().freeSelect("color").title("Color").description("Your color").required(false).defaultValue("Blue").values("Blue", "Beige", "Black").build())
.property(PropertyBuilder.builder().integer("many").title("Many").description("How many?").required(false).defaultValue("2").build())
.property(PropertyBuilder.builder().longType("cramp").title("Cramp").description("How crampy more?").required(false).defaultValue("20").build())
.property(PropertyBuilder.builder().select("rice").title("Rice Cream").description("Rice Cream Flavor").required(false).values("Flambe", "Crambo").build()).build();

11.0.3, x86_64:	"Java SE 11.0.3"	/Library/Java/JavaVirtualMachines/jdk-11.0.3.jdk/Contents/Home
    1.8.0_241, x86_64:	"Java SE 8"	/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home
    1.8.0_91, x86_64:	"Java SE 8"	/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home
    1.7.0_71, x86_64:	"Java SE 7"	/Library/Java/JavaVirtualMachines/jdk1.7.0_71.jdk/Contents/Home
    1.7.0_65, x86_64:	"Java SE 7"	/Library/Java/JavaVirtualMachines/jdk1.7.0_65.jdk/Contents/Home
 */
