package com.github.three_old_coders.blueprint.java_agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.MemberSubstitution;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class FileReplaceAgent
{
    public static void main(final String[] args)
    {
        new AgentBuilder
            .Default()
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
            .type(nameStartsWith(""))
            .transform((builder, typeDescription, classLoader, module) -> builder.visit(MemberSubstitution.strict()
//                    .constructor(isDeclaredBy(File.class))
//                    .replaceWith(FileFiltering.class.getMethod("file", String.class))
//                    .on(any()))
                .method(ElementMatchers.isPublic())
                .intercept(MethodDelegation.to(PublicInterceptor.class))
                .method(ElementMatchers.named("foo"))
                .intercept(MethodDelegation.to(FooInterceptor.class))

                .installOnByteBuddyAgent();
    }
}
