package de._3oc_.t55bs4.services;

import de._3oc_.t55bs.services.BootstrapCommonModule;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

import static de._3oc_.t55bs4.services.Bootstrap4Stack.PATH_BS4_S;

@ImportModule(BootstrapCommonModule.class)
@SuppressWarnings("unused")
public class Bootstrap4Module
{
    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void setupEnvironment(final MappedConfiguration<String, Object> configuration)
    {
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        configuration.add(SymbolConstants.BOOTSTRAP_ROOT, PATH_BS4_S);
    }

    public static void contributeJavaScriptStackSource(final MappedConfiguration<String, JavaScriptStack> configuration)
    {
        configuration.addInstance(Bootstrap4Stack.class.getSimpleName(), Bootstrap4Stack.class);
    }

    public static void contributeComponentClassResolver(final Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("t55bs", "de._3oc_.t55bs4"));
    }
}
