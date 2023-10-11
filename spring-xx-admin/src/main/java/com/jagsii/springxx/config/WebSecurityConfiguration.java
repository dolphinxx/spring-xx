package com.jagsii.springxx.config;

import com.jagsii.springxx.common.captcha.CaptchaFilter;
import com.jagsii.springxx.common.security.*;
import com.jagsii.springxx.common.utils.ConfigUtils;
import com.jagsii.springxx.common.web.DelegatedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebSecurityConfiguration {
    private final CaptchaFilter captchaFilter;

    public WebSecurityConfiguration(@Autowired(required = false) CaptchaFilter captchaFilter) {
        this.captchaFilter = captchaFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(PlatformProperties platformProperties) {
        return (web) -> {
            // URIs ignored by web security
            if (platformProperties.getSecurity() != null && platformProperties.getSecurity().getPermitAll() != null) {
                WebSecurity.IgnoredRequestConfigurer ignored = web.ignoring();
                // platform:
                //   security:
                //    permit-all:
                //      ## home
                //      - /
                //      ## favicon
                //      - /favicon.ico
                //      ## login submit
                //      - POST:/login
                //      ## static resources
                //      - /static/**
                ignored.requestMatchers(platformProperties.getSecurity().getPermitAll().stream().map(ConfigUtils::parseMatcher).toArray(AntPathRequestMatcher[]::new));
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PersistentTokenRepository persistentTokenRepository) throws Exception {
        http.authorizeRequests(requests -> requests.anyRequest().authenticated().accessDecisionManager(webAccessDecisionManager()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(customizer -> customizer.successHandler(restAuthenticationSuccessHandler()).failureHandler(restAuthenticationFailureHandler()))
                .logout(customizer -> customizer.logoutSuccessHandler(restLogoutSuccessHandler()))
                .httpBasic(customizer -> customizer.authenticationEntryPoint(authenticationEntryPoint()))
                .rememberMe(customizer -> customizer.tokenRepository(persistentTokenRepository))
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(authenticationEntryPoint()).accessDeniedHandler(restAccessDeniedHandler()));
        if (captchaFilter != null) {
            http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
        }
        return http.build();
    }

    /**
     * Exclude {@link PreInvocationAuthorizationAdviceVoter}
     */
    @Bean
    public AccessDecisionManager webAccessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>();
        voters.add(new WebExpressionVoter());
        voters.add(new AuthenticatedVoter());
        voters.add(new Jsr250Voter());
        voters.add(new RoleVoter());
        voters.add(new PermVoter());
        return new AffirmativeBased(voters);
    }

    @Bean
    public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    @Bean
    public RestAuthenticationFailureHandler restAuthenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public RestAccessDeniedHandler restAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    public RestLogoutSuccessHandler restLogoutSuccessHandler() {
        return new RestLogoutSuccessHandler();
    }

    @Bean
    public DelegatedAuthenticationEntryPoint authenticationEntryPoint() {
        return new DelegatedAuthenticationEntryPoint();
    }

    @Bean
    @Profile("!test")
    public PersistentTokenRepository rememberMeTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl bean = new JdbcTokenRepositoryImpl();
        bean.setDataSource(dataSource);
        return bean;
    }
}
