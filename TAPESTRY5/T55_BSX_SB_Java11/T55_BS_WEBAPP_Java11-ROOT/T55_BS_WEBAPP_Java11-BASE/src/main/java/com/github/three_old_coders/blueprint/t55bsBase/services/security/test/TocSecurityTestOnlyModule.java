package com.github.three_old_coders.blueprint.t55bsBase.services.security.test;

import com.github.three_old_coders.blueprint.t55bsBase.services.security.ITocSecurityService;
import com.github.three_old_coders.blueprint.t55bsBase.services.security.TocSecurityService;
import org.apache.shiro.ShiroException;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.RequestExceptionHandler;
import org.apache.tapestry5.services.ResponseRenderer;
import org.slf4j.Logger;
import org.tynamo.security.services.SecurityService;

@SuppressWarnings("unused")
public class TocSecurityTestOnlyModule
{
    @Contribute(WebSecurityManager.class)
    public static void addRealms(final Configuration<Realm> configuration)
    {
        try {
            configuration.add(new TocSecurityRealmTestOnly());
        } catch (final Exception e) {
            throw new IllegalStateException("unable to initialize realm", e);
        }
    }

    @Scope(ScopeConstants.PERTHREAD)
    public static ITocSecurityService buildTocSecurityService(final SecurityService securityService, final Request request)
    {
        return new TocSecurityService(securityService, request);
    }

    // http://tapestry.apache.org/overriding-exception-reporting.html
    public RequestExceptionHandler buildAppRequestExceptionHandler(final Logger logger,
                                                                   final ResponseRenderer renderer,
                                                                   final ComponentSource componentSource)
    {
        return exception -> {
            if (exception instanceof ShiroException) {
                // does not work: throw (ShiroException)exception;
                renderer.renderPageMarkupResponse("NotAuthorized"); // see: contributeApplicationDefaults
            } else {
                logger.error("Unexpected runtime exception: " + exception.getMessage(), exception);

                final Component c = componentSource.getPage("Index");

                if (c instanceof ExceptionReporter) {
                    final ExceptionReporter index = (ExceptionReporter) c;
                    index.reportException(exception);
                }

                renderer.renderPageMarkupResponse("Index");
            }
        };
    }

    public void contributeServiceOverride(final MappedConfiguration<Class, Object> configuration,
                                          final @Local RequestExceptionHandler handler)
    {
        configuration.add(RequestExceptionHandler.class, handler);
    }
}
