package com.github.three_old_coders.blueprint.t55bs4.services;

import com.github.three_old_coders.blueprint.t55bs.services.BootstrapCommonModule;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

@ImportModule(BootstrapCommonModule.class)
@SuppressWarnings("unused")
public class Bootstrap4Module
{
    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void setupEnvironment(final MappedConfiguration<String, Object> configuration)
    {
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        configuration.add(SymbolConstants.BOOTSTRAP_ROOT, Bootstrap4Stack.PATH_BS4_S);
    }

    public static void contributeJavaScriptStackSource(final MappedConfiguration<String, JavaScriptStack> configuration)
    {
        configuration.addInstance(Bootstrap4Stack.class.getSimpleName(), Bootstrap4Stack.class);
    }

    public static void contributeComponentClassResolver(final Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("t55bs", "com.github.three_old_coders.blueprint.t55bs4"));
    }
}
