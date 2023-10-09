package com.jagsii.springxx.common.security;

import org.springframework.security.core.GrantedAuthority;

public class PermGrantedAuthority implements GrantedAuthority {
    private final String raw;
    private final PermissionData perm;

    public PermGrantedAuthority(String perm) {
        this.raw = perm;
        this.perm = new PermissionData(perm);
    }

    public String getRaw() {
        return raw;
    }

    public PermissionData getPerm() {
        return perm;
    }

    @Override
    public String getAuthority() {
        return PermVoter.PERM_PREFIX + perm;
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
