package com.github.three_old_coders.blueprint.t55bssb.services;

import com.github.three_old_coders.blueprint.t55bsBase.services.T55AppBaseModule;
import info.code8.tapestry.TapestryApplication;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.http.Link;
import org.apache.tapestry5.http.services.Response;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.RequestExceptionHandler;

@TapestryApplication
@ImportModule(T55AppBaseModule.class)
public class ApplicationModule
{
    /**
     * https://tapestry.apache.org/specific-errors-faq.html
     * Redirect the user to the intended page when browsing through
     * tapestry forms through browser history or over-eager autocomplete
     */
    public RequestExceptionHandler decorateRequestExceptionHandler(final ComponentSource componentSource,
                                                                   final Response response,
                                                                   final RequestExceptionHandler oldHandler)
    {
        return exception -> {
            if (null != exception && null != exception.getMessage()
                && (exception.getMessage().contains("Forms require that the request method be POST and that the t:formdata query parameter have values")
                || (exception.getMessage().contains("A component event handler method returned the value Block"))
            )
            ) {
                final ComponentResources cr = componentSource.getActivePage().getComponentResources();
                final Link link = cr.createEventLink("");
                String uri = link.toRedirectURI().replaceAll(":", "");
                response.sendRedirect(uri);
            } else {
                oldHandler.handleRequestException(exception);
            }
        };
    }
}
