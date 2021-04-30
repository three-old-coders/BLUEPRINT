package com.github.three_old_coders.blueprint.t55bsBase.services.security;

import java.util.Collections;
import java.util.Set;

public class TocSecurityUserContainer
{
    private final ITocSecurityUser _user;
    private final Set<String> _roles;
    private final Set<String> _permissions;

    public TocSecurityUserContainer(final ITocSecurityUser user, final Set<String> roles, final Set<String> permissions)
    {
        _user = user;
        _roles = roles;
        _permissions = Collections.unmodifiableSet(permissions);
    }

    public ITocSecurityUser getUser()
    {
        return _user;
    }

    public Set<String> getRoles()
    {
        return _roles;
    }

    public Set<String> getPermissions()
    {
        return _permissions;
    }
}
