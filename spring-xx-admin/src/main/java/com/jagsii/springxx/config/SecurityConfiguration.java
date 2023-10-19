package com.jagsii.springxx.config;

import com.jagsii.springxx.common.security.AuthorityPermissionEvaluator;
import com.jagsii.springxx.common.security.PermVoter;
import com.jagsii.springxx.common.security.access.HasPermAnnotationMetadataExtractor;
import com.jagsii.springxx.common.security.access.annotation.HasPermVoter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.SpringSecurityMessageSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Bean
    @Override
    public AccessDecisionManager accessDecisionManager() {
        AffirmativeBased affirmativeBased = (AffirmativeBased) super.accessDecisionManager();
        affirmativeBased.getDecisionVoters().add(new HasPermVoter());
        affirmativeBased.getDecisionVoters().add(new PermVoter());
        return affirmativeBased;
    }

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return new SecuredAnnotationSecurityMetadataSource(new HasPermAnnotationMetadataExtractor());
    }

    @Bean
    public MessageSourceAccessor springSecurityMessageSourceAccessor() {
        return SpringSecurityMessageSource.getAccessor();
    }

    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new AuthorityPermissionEvaluator();
    }
}
