package com.github.three_old_coders.blueprint.t55bsBase.services.security;

import java.util.Set;

public interface ITocSecurityUser
{
    String getLoginName();

    Set<String> getRoles();
    // --> getPermissions()
}
