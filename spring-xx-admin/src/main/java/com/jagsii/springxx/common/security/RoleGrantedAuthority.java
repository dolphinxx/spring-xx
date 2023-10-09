package com.jagsii.springxx.common.security;

import org.springframework.security.core.GrantedAuthority;

public class RoleGrantedAuthority implements GrantedAuthority {
    private final String role;

    public RoleGrantedAuthority(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + role;
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
