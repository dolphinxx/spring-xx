package com.jagsii.springxx.config;

import com.jagsii.springxx.common.captcha.CaptchaFilter;
import com.jagsii.springxx.common.security.RestAccessDeniedHandler;
import com.jagsii.springxx.common.security.RestAuthenticationFailureHandler;
import com.jagsii.springxx.common.security.RestAuthenticationSuccessHandler;
import com.jagsii.springxx.common.security.RestLogoutSuccessHandler;
import com.jagsii.springxx.common.utils.ConfigUtils;
import com.jagsii.springxx.common.web.DelegatedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfiguration {
    private final CaptchaFilter captchaFilter;
    private final ConfigurableEnvironment env;

    public WebSecurityConfiguration(@Autowired(required = false) CaptchaFilter captchaFilter, ConfigurableEnvironment env) {
        this.captchaFilter = captchaFilter;
        this.env = env;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            if (env.acceptsProfiles(Profiles.of("dev"))) {
                web.debug(true);
            }
            // Ignore well-known URIs
            web.ignoring().antMatchers("/favicon.ico");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PersistentTokenRepository persistentTokenRepository, PlatformProperties platformProperties) throws Exception {
        http.authorizeRequests(customizer -> {
                    if (platformProperties.getSecurity() != null && platformProperties.getSecurity().getPermitAll() != null) {
                        // platform: ## format [{METHOD}:]{URI}
                        //   security:
                        //    permit-all:
                        //      ## home
                        //      - /
                        //      ## login submit
                        //      - POST:/login
                        //      ## static resources
                        //      - /static/**
                        customizer.requestMatchers(platformProperties.getSecurity().getPermitAll().stream().map(ConfigUtils::parseMatcher).toArray(AntPathRequestMatcher[]::new)).permitAll();
                    }
                    customizer.anyRequest().authenticated();
                })
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
    public PersistentTokenRepository rememberMeTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }
}
