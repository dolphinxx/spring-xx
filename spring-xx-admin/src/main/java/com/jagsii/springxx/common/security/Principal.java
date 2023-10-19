package com.jagsii.springxx.common.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@JsonIgnoreProperties({"password", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
public class Principal extends User {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID * 1000 + 1L;
    @Getter
    private final String name;

    public Principal(String name, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.name = name;
    }
}
