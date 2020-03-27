package de._3oc_.t55bsBase.services;

import de._3oc_.t55bs4.services.Bootstrap4Module;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.services.LibraryMapping;

@ImportModule(Bootstrap4Module.class)
@SuppressWarnings("unused")
public class T55AppBaseModule
{
    public static void contributeComponentClassResolver(final Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("", "de._3oc_.t55bsBase"));
    }
}
