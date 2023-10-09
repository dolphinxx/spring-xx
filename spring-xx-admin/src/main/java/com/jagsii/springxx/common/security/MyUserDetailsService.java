package com.jagsii.springxx.common.security;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface MyUserDetailsService extends UserDetailsService {
    void reloadAuthorities();
}
