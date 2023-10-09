package com.jagsii.springxx.config;

import com.jagsii.springxx.common.security.PermVoter;
import com.jagsii.springxx.common.security.AuthorityPermissionEvaluator;
import com.jagsii.springxx.common.security.expression.ExtMethodSecurityExpressionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.SpringSecurityMessageSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Bean
    @Override
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>();
//        voters.add(new WebExpressionVoter());
        voters.add(new AuthenticatedVoter());
        voters.add(new Jsr250Voter());
        ExpressionBasedPreInvocationAdvice expressionBasedPreInvocationAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionBasedPreInvocationAdvice.setExpressionHandler(getExpressionHandler());
        voters.add(new PreInvocationAuthorizationAdviceVoter(expressionBasedPreInvocationAdvice));
        voters.add(new RoleVoter());
        voters.add(new PermVoter());
        return new AffirmativeBased(voters);
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        ExtMethodSecurityExpressionHandler handler = new ExtMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator());
        return handler;
    }

    @Bean
    public PermissionEvaluator permissionEvaluator() {
        return new AuthorityPermissionEvaluator();
    }

    @Bean
    public MessageSourceAccessor springSecurityMessageSourceAccessor() {
        return SpringSecurityMessageSource.getAccessor();
    }
}
