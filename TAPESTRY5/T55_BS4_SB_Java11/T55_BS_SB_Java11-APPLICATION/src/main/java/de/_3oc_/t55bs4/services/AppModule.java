package de._3oc_.t55bs4.services;

import info.code8.tapestry.TapestryApplication;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.services.LibraryMapping;

@TapestryApplication
@ImportModule({Bootstrap4Module.class})
@SuppressWarnings("unused")
public class AppModule
{
    public static void contributeComponentClassResolver(final Configuration<LibraryMapping> configuration)
    {
        // todo: choose bootstrap version dynamically?
    }
}
