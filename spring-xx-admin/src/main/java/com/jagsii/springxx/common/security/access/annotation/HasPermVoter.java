package com.jagsii.springxx.common.security.access.annotation;

import com.jagsii.springxx.common.security.AuthorityPermissionEvaluator;
import com.jagsii.springxx.common.security.access.HasPermSecurityConfig;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class HasPermVoter implements AccessDecisionVoter<Object> {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof HasPermSecurityConfig;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }
        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;
                if (AuthorityPermissionEvaluator.hasPermission(authorities, attribute.getAttribute())) {
                    return ACCESS_GRANTED;
                }
            }
        }
        return result;
    }
}
