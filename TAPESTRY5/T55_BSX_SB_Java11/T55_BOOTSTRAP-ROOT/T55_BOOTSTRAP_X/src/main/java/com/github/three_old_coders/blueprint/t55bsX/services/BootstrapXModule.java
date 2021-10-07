package com.github.three_old_coders.blueprint.t55bsX.services;

import com.github.three_old_coders.blueprint.t55bs.services.BootstrapCommonModule;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.commons.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.Core;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StackExtension;

@ImportModule(BootstrapCommonModule.class)
@SuppressWarnings("unused")
public class BootstrapXModule
{
    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public static void setupEnvironment(final MappedConfiguration<String, Object> configuration)
    {
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        // configuration.add(SymbolConstants.BOOTSTRAP_ROOT, Bootstrap4Stack.PATH_BS4_S);
    }

    public static void contributeJavaScriptStackSource(final MappedConfiguration<String, JavaScriptStack> configuration)
    {
        configuration.addInstance(BootstrapXStack.class.getSimpleName(), BootstrapXStack.class);
    }

    public static void contributeComponentClassResolver(final Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("t55bs", "com.github.three_old_coders.blueprint.t55bsX"));
    }

    // ---->> remove T5 css / js
    // http://apache-tapestry-mailing-list-archives.1045711.n5.nabble.com/T5-4-Removing-Bootstrap-from-pages-individually-td5723955.html
    public void contributeMarkupRenderer(final OrderedConfiguration<MarkupRendererFilter> conf)
    {
        conf.override("ImportCoreStack", null);
    }

    @Contribute(JavaScriptStack.class) @Core
    public static void setupCoreJavaScriptStack(final OrderedConfiguration<StackExtension> conf)
    {
        conf.override("bootstrap.css", null);
    }
}
