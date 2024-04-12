package com.github.three_old_coders.blueprint;

import com.github.three_old_coders.blueprint.spring.IMessageHandler;
import com.github.three_old_coders.blueprint.spring.MessageDesc;
import org.pf4j.PluginManager;

import java.util.List;

public class RunMethodUtils
{
    private RunMethodUtils() {}


    public static void check_canHandle_1_2(final PluginManager pluginManager)
    {
        final List<IMessageHandler> extensions = pluginManager.getExtensions(IMessageHandler.class);

        int countHandled = 0;

        {
            final MessageDesc md = new MessageDesc("P1");
            for (final IMessageHandler extension : extensions) {
                countHandled += extension.canHandle(md) ? 1 : 0;
            }
        }

        {
            final MessageDesc md = new MessageDesc("P2");
            for (final IMessageHandler extension : extensions) {
                countHandled += extension.canHandle(md) ? 1 : 0;
            }
        }

        final MessageDesc md = new MessageDesc("P3");
        for (final IMessageHandler extension : extensions) {
            countHandled += extension.canHandle(md) ? 1 : 0;
        }

        System.out.println("handled [" + countHandled + "] messages from [3]");
    }
}
