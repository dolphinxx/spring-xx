package com.jagsii.springxx.common.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public class AuthorityPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return hasPermission(authentication.getAuthorities(), (String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication.getAuthorities(), (String) permission);
    }

    public static boolean hasPermission(Collection<? extends GrantedAuthority> authorities, String permission) {
        PermissionData permData = new PermissionData(permission);
        // Attempt to find a matching granted authority
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof PermGrantedAuthority && ((PermGrantedAuthority) authority).getPerm().hasPermission(permData)) {
                return true;
            }
        }
        return false;
    }
}
