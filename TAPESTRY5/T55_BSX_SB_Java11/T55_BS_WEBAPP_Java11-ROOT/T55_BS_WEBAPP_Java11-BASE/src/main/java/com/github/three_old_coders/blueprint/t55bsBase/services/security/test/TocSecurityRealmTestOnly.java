package com.github.three_old_coders.blueprint.t55bsBase.services.security.test;

import com.github.three_old_coders.blueprint.t55bsBase.services.security.ITocSecurityUser;
import com.github.three_old_coders.blueprint.t55bsBase.services.security.TocSecurityRealmBase;

import java.util.HashSet;
import java.util.Set;

public class TocSecurityRealmTestOnly
    extends TocSecurityRealmBase
{
    public TocSecurityRealmTestOnly()
    {
        super(String.class, "application");
    }

    @Override
    protected Set<String> createPermissions(final ITocSecurityUser user, final Set set)
    {
        return set;     // copy roles to permissions
    }

    @Override
    protected ITocSecurityUser findUser(final Class aClass, final String s)
    {
        final Set<String> roles = new HashSet<>();
        final String email = s.toLowerCase();
        if (email.endsWith("@test.bla")) {
            if (email.startsWith("admin@")) {
                roles.add("*");
            }

            roles.add("WHATEVER");
        } else {
            roles.add("WHATELSE");
        }

        return new ITocSecurityUser()
        {
            @Override public String getLoginName() { return email; }

            @Override public Set<String> getRoles() { return roles; }
        };
    }
}
