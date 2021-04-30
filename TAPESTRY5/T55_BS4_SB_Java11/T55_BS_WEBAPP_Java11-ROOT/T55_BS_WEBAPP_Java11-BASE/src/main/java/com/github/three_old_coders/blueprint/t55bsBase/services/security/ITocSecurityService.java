package com.github.three_old_coders.blueprint.t55bsBase.services.security;

public interface ITocSecurityService
{
    TocSecurityUserContainer getSecurityUserContainer();
    boolean hasRole(final String... roles);
    String getOauthRefreshToken();
}
