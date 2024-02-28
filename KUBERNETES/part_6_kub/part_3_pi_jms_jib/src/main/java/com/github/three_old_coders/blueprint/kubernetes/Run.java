package com.github.three_old_coders.blueprint.kubernetes;

import lombok.SneakyThrows;
import picocli.CommandLine;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class Run
{
    public static void main(final String[] args)
    {
        final int result = new CommandLine(new Executor()).execute(args);
        System.exit(result);
    }

    public static class Executor implements Callable<Integer>
    {
        @CommandLine.Option(names = {"-c", "--class"}, description = "Java class to execute", required = true)
        String _class;

        @CommandLine.Option(names = {"-args", "--args"}, description = "Arguments list", required = false)
        String[] _params;

        @SneakyThrows
        public Integer call()
        {
            final Class<?> c2e = Class.forName(_class);
            final Method mm = c2e.getMethod("main", String[].class);
            if (null == _params) {
                mm.invoke(null, (Object)new String[0]);
            } else {
                mm.invoke(null, (Object)_params);
            }

            return 0;
        }
    }
}
