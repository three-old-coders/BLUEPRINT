package com.github.three_old_coders.blueprint.spring;

import org.pf4j.Extension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringPF4JPlugin1
    extends SpringPF4JPluginBase
{
    public static final String PLUGIN_NAME = "Spring-Plugin-1";

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

    //
    // ---->> PLUGIN EXTENSION (IMessageHandler)
    //

    @Extension
    public static class HandleExtension extends SpringExtensionBase
        implements IMessageHandler
    {
        public HandleExtension(final ApplicationContext applicationContext)
        {
            super(applicationContext);
        }

        @Override
        protected String[] getPluginScanPackages()
        {
            return new String[] {"com.github.three_old_coders.blueprint.spring"};
        }

        @Override
        protected void updateApplicationContext(final AnnotationConfigApplicationContext applicationContext)
        {
            applicationContext.register(SpringConfiguration.class);
        }

        //
        // ---->> IMessageHandler
        //

        @Override
        public boolean canHandle(final MessageDesc message)
        {
            if ("P1".equals(message.getType())) {
                System.out.println(PLUGIN_NAME + " can handle message " + message);
                final PrivatePluginService pps = getApplicationContext().getBean(PrivatePluginService.class);
                pps.doSomething();

                return true;
            }

            return false;
        }
    }
}
