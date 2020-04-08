package com.github.three_old_coders.blueprint.docker;

import io.github.classgraph.*;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public abstract class DockerInfoBase
{
    protected static void internalMain(final String[] args)
    {
        final PrintStream psErr = System.err;
        
        System.out.println("Classes you can run:");
        try (final ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages("de._3oc_").scan()) {
            for (final ClassInfo ci : scanResult.getAllClasses()) {
                final String className = ci.getName();
                if (! className.equals(DockerInfoBase.class.getName())) {
                    final MethodInfoList miMain = ci.getMethodInfo("main");
                    if (miMain.size() > 0) {
                        final AnnotationInfo ai = ci.getAnnotationInfo("picocli.CommandLine$Command");
                        if (null == ai) {
                            System.out.println(className);
                        } else {
                            try {
                                final Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                                final Object o = aClass.getDeclaredConstructor().newInstance();

                                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                System.setErr(new PrintStream(baos, true, StandardCharsets.UTF_8));
                                new CommandLine(o).execute(args);
                                String s = baos.toString();
                                final int index = s.indexOf("Usage: ");
                                if (index > 0) {
                                    s = s.substring(index);
                                }

                                System.out.println(s);
                            } catch (final Exception e) {
                                System.out.println(className);
                            } finally {
                                System.setErr(psErr);
                            }
                        }

                        System.out.println("");
                    }
                }
            }
        }
    }
}
