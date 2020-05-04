package com.github.three_old_coders.blueprint.rundeck;

import io.github.classgraph.*;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.util.*;

public class PicoliUtils
{
    private PicoliUtils()
    {
    }

    public static JSONObject convert(final String id, final CommandLine cl)
    {
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

        final JSONArray ja = new JSONArray();
        for (CommandLine.Model.OptionSpec o : options) {
            ja.put(new JSONObject(
                "isOption", o.isOption(), "required", o.required(), "type", o.type().getName(),
                "shortestName", o.shortestName(), "longestName", o.longestName(), "defaultValue", o.defaultValue(),
                "label", o.paramLabel(), "descriptions", StringUtils.join(o.description(), "\n")
            ));
        }
        for (CommandLine.Model.PositionalParamSpec p : params) {
            ja.put(new JSONObject(
                "isPositional", p.isPositional(), "required", p.required(), "index", p.index().min(), "type", p.type().getName(), "defaultValue", p.defaultValue(),
                "label", p.paramLabel(), "descriptions", StringUtils.join(p.description(), "\n")
            ));
        }
        // PropertyBase{name='arg 0 resultFileA', title='<resultFileA>', description='result file A', required=true, renderingOptions={}, blankIfUnexpanded=true}
        return new JSONObject("id", id, "args", ja);
    }

    public static CommandLine createFrom(final Class<?> clazz)
    {
        try (final ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistClasses(clazz.getName()).scan()) {
            for (final ClassInfo ci : scanResult.getAllClasses()) {
                final String className = ci.getName();
                final MethodInfoList miMain = ci.getMethodInfo("main");
                if (miMain.size() > 0) {
                    final AnnotationInfo ai = ci.getAnnotationInfo("picocli.CommandLine$Command");
                    if (null != ai) {
                        final Class<?> aClass = clazz.getClassLoader().loadClass(className);
                        final Object object = aClass.getDeclaredConstructor().newInstance();
                        return new CommandLine(object);
                    }
                }
            }

            throw new NullPointerException("missing picoli command line");
        } catch (final Exception e) {
            throw new IllegalStateException("unable to create JSON description", e);
        }
    }

    public static Map<Class<?>, JSONObject> createJSONsFrom(final Class<?> ... picoliClasses)
    {
        final Map<Class<?>, JSONObject> map = new HashMap<>();
        for (final Class<?> picoliClass : picoliClasses) {
            final String cn = picoliClass.getName();
            final JSONObject jo = convert(cn, createFrom(picoliClass));
            map.put(picoliClass, jo);
        }

        return map;
    }
}
