package com.github.three_old_coders.blueprint.t55bsBase.services.security;

import org.apache.shiro.util.StringUtils;
import org.apache.tapestry5.http.services.Request;
import org.tynamo.security.services.SecurityService;

import java.util.Collections;
import java.util.Set;

/**
 * The type Toc security service.
 */
public class TocSecurityService implements ITocSecurityService
{
    private final SecurityService _shiroSecurityService;
    protected final Request _request;
    private final String _refreshToken;

    /**
     * Instantiates a new Toc security service.
     *
     * @param shiroSecurityService the shiro security service
     * @param request              the request
     */
    public TocSecurityService(final SecurityService shiroSecurityService, final Request request)
    {
        this(shiroSecurityService, request, null);
    }

    /**
     * Instantiates a new Toc security service.
     *
     * @param shiroSecurityService the shiro security service
     * @param request              the request
     * @param refreshToken         the oauth refresh token
     */
    public TocSecurityService(final SecurityService shiroSecurityService, final Request request, final String refreshToken)
    {
        _shiroSecurityService = shiroSecurityService;
        _request = request;
        _refreshToken = refreshToken;
    }

    //
    //
    //

    public TocSecurityUserContainer getSecurityUserContainer()
    {
        final TocSecurityUserContainer principal = (TocSecurityUserContainer) _shiroSecurityService.getSubject().getPrincipal();
        return null != principal ? principal : new TocSecurityUserContainer(null, Collections.emptySet(), Collections.emptySet());
    }

    /**
     * supports "any" e.g. Vendor:* returns true if at least one Vendor role exists
     * @param roles the roles
     * @return boolean
     */
    public boolean hasRole(final String... roles)
    {
        try {
            final TocSecurityUserContainer securityUserContainer = getSecurityUserContainer();
            final ITocSecurityUser user = securityUserContainer.getUser();
            if (null != user && null != roles) {
                final Set<String> userRoles = user.getRoles();
                for (final String role : roles) {
                    if (userRoles.contains(role)) {
                        return true;
                    } else {
                        final String[] splitNameAndValue = StringUtils.split(role, ':');
                        final String roleTyp = splitNameAndValue[0] + ":";
                        final String roleCharacteristic = splitNameAndValue[1]; // "*" == we do not mind, at least one characteristic has to exist
                        final String adminRole = roleTyp + "*";
                        for (final String userRole : userRoles) {
                            if (userRole.equals(adminRole) || roleCharacteristic.equals("*") && userRole.startsWith(roleTyp)) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            // do nothing --> error --> no rights
        }

        return false;
    }

    public String getOauthRefreshToken()
    {
        return _refreshToken;
    }
}
