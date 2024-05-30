package com.github.three_old_coders.blueprint.spring;

import org.pf4j.Extension;
import org.pf4j.Plugin;

public class PF4JPlugin2
    extends Plugin
{
    public static final String PLUGIN_NAME = "Plugin-2";

    // ---->> PLUGIN LIFECYCLE

    @Override
    public void start()
    {
        System.out.println(PLUGIN_NAME + " started");
    }

    @Override
    public void stop()
    {
        System.out.println(PLUGIN_NAME + " stopped");
    }

    @Override
    public void delete()
    {
        System.out.println(PLUGIN_NAME + " to be deleted");
    }

    // ---->> PLUGIN INTERFACE (IMessageHandler)

    @Extension
    public static class HandleExtension implements IMessageHandler
    {
        @Override
        public boolean canHandle(final MessageDesc message)
        {
            if ("P2".equals(message.getType())) {
                System.out.println(PLUGIN_NAME + " can handle message " + message);
                return true;
            }

            return false;
        }
    }
}
