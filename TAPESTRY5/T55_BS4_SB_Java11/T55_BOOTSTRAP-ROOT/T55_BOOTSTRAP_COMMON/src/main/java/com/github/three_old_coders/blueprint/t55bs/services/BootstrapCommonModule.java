package com.github.three_old_coders.blueprint.t55bs.services;

import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.services.LibraryMapping;

@SuppressWarnings("unused")
public class BootstrapCommonModule
{
    public static void contributeComponentClassResolver(final Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("t55bs", "com.github.three_old_coders.blueprint.t55bs"));
    }
}
