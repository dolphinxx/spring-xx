package com.jagsii.springxx.config;

import com.jagsii.springxx.common.security.PermVoter;
import com.jagsii.springxx.common.security.RestAuthenticationFailureHandler;
import com.jagsii.springxx.common.security.RestAuthenticationSuccessHandler;
import com.jagsii.springxx.common.security.RestLogoutSuccessHandler;
import com.jagsii.springxx.common.web.DelegatedAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebSecurityConfiguration {
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
                for (String p : platformProperties.getSecurity().getPermitAll()) {
                    if (p.contains(":")) {
                        String[] tuple = p.split(":");
                        ignored.antMatchers(HttpMethod.resolve(tuple[0].trim().toUpperCase()), tuple[1].trim());
                    } else {
                        ignored.antMatchers(p);
                    }
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PersistentTokenRepository persistentTokenRepository) throws Exception {
        http.authorizeRequests(requests -> {
//                    if (platformProperties.getSecurity().getPermitAll() != null) {
//                        // platform:
//                        //   security:
//                        //    permit-all:
//                        //      ## home
//                        //      - /
//                        //      ## favicon
//                        //      - /favicon.ico
//                        //      ## login submit
//                        //      - POST:/login
//                        //      ## static resources
//                        //      - /static/**
//                        List<AntPathRequestMatcher> permitAllMatchers = new ArrayList<>(platformProperties.getSecurity().getPermitAll().size());
//                        for (String p : platformProperties.getSecurity().getPermitAll()) {
//                            if (p.contains(":")) {
//                                String[] tuple = p.split(":");
//                                permitAllMatchers.add(new AntPathRequestMatcher(tuple[1].trim(), tuple[0].trim().toUpperCase()));
//                            } else {
//                                permitAllMatchers.add(new AntPathRequestMatcher(p));
//                            }
//                        }
//                        if (permitAllMatchers.size() > 0) {
//                            requests.requestMatchers(permitAllMatchers.toArray(new AntPathRequestMatcher[0])).permitAll();
//                        }
//                    }
                    requests.anyRequest().authenticated().accessDecisionManager(webAccessDecisionManager());
                }).csrf().disable()
                .formLogin((form) -> form.successHandler(restAuthenticationSuccessHandler()).failureHandler(restAuthenticationFailureHandler()))
                .logout(configurer -> configurer.logoutSuccessHandler(restLogoutSuccessHandler()))
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authenticationEntryPoint()))
                .rememberMe(configurer -> configurer.tokenRepository(persistentTokenRepository))
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());
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
