package com.github.three_old_coders.blueprint.t55bsBase.services.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

public abstract class TocSecurityRealmBase<T extends ITocSecurityUser>
    extends AuthorizingRealm
{
    protected final Class<T> _userEntityClass;
    protected final String _realmName;

    public TocSecurityRealmBase(final Class<T> userEntityClass, final String realmName)
    {
        super(new MemoryConstrainedCacheManager());

        _userEntityClass = userEntityClass;
        _realmName = realmName;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals)
    {
        final TocSecurityUserContainer securityUser = (TocSecurityUserContainer) principals.getPrimaryPrincipal();
        final Set<String> unusedRealmNames = principals.getRealmNames();

        final SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo(securityUser.getRoles());
        sai.addStringPermissions(securityUser.getPermissions());

        return sai;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
        throws AuthenticationException
    {
        final String userName = (String) token.getPrincipal();       // is email
        if (StringUtils.isBlank(userName)) {
            throw new UnknownAccountException("user name missing");
        }

        final T user = findUser(_userEntityClass, userName);
        if (null == user) {
            throw new UnknownAccountException("user [" + userName + "] unknown");
        }

        // ---- which roles do we have?
        final Set<String> roles = createRoles(user);
        // ---- add permissions based on roles
        final Set<String> permissions = createPermissions(user, roles);

        final TocSecurityUserContainer securityUser = new TocSecurityUserContainer(user, roles, permissions);
        final SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(securityUser, token.getCredentials(), _realmName);

        return sai;
    }

    protected Set<String> createRoles(final T user)
    {
        return user.getRoles();
    }

    protected abstract Set<String> createPermissions(final T user, final Set<String> roles);

    protected abstract T findUser(final Class<T> userEntityClass, final String id);
}
