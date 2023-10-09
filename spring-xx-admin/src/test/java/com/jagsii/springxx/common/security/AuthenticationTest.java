package com.jagsii.springxx.common.security;

import com.jagsii.springxx.WebTests;
import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.common.web.RestExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({
        RestExceptionHandler.class,
        AuthenticationTest.TestController.class,
})
public class AuthenticationTest extends WebTests {
    @Autowired
    MockMvc mvc;
    @MockBean
    UserDetailsService userDetailsService;

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
    }

    @Test
    void httpBasic() throws Exception {
        Principal principal = new Principal("Foo", "foo", "{noop}123456", Collections.emptyList(), true, true, true, true);
        Mockito.when(userDetailsService.loadUserByUsername("foo")).thenReturn(principal);
        String authHeader = BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC + " " + Base64.getEncoder().encodeToString("foo:123456".getBytes(StandardCharsets.UTF_8));
        mvc.perform(post("/test/greeting").header(HttpHeaders.AUTHORIZATION, authHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Hello World!")));
        Mockito.verify(userDetailsService).loadUserByUsername("foo");
    }

    @Test
    void httpBasic_badCredential() throws Exception {
        Principal principal = new Principal("Foo", "foo", "{noop}123456", Collections.emptyList(), true, true, true, true);
        Mockito.when(userDetailsService.loadUserByUsername("foo")).thenReturn(principal);
        String authHeader = BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC + " " + Base64.getEncoder().encodeToString("foo:12345".getBytes(StandardCharsets.UTF_8));
        mvc.perform(post("/test/greeting").header(HttpHeaders.AUTHORIZATION, authHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(401)));
        Mockito.verify(userDetailsService).loadUserByUsername("foo");
    }

    @Test
    void httpBasic_rememberMe() throws Exception {
        Principal principal = new Principal("Foo", "foo", "{noop}123456", Collections.emptyList(), true, true, true, true);
        Mockito.when(userDetailsService.loadUserByUsername("foo")).thenReturn(principal);
        String authHeader = BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC + " " + Base64.getEncoder().encodeToString("foo:123456".getBytes(StandardCharsets.UTF_8));
        Cookie rememberMe = mvc.perform(post("/test/greeting").header(HttpHeaders.AUTHORIZATION, authHeader).param("remember-me", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("remember-me"))
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Hello World!")))
                .andReturn().getResponse().getCookie("remember-me");
        Mockito.verify(userDetailsService).loadUserByUsername("foo");
        Assertions.assertNotNull(rememberMe);
        mvc.perform(post("/test/greeting").cookie(new MockCookie("remember-me", rememberMe.getValue())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Hello World!")));
    }

    @Test
    void httpBasic_logout() throws Exception {
        Principal principal = new Principal("Foo", "foo", "{noop}123456", Collections.emptyList(), true, true, true, true);
        Mockito.when(userDetailsService.loadUserByUsername("foo")).thenReturn(principal);
        String authHeader = BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC + " " + Base64.getEncoder().encodeToString("foo:123456".getBytes(StandardCharsets.UTF_8));
        Cookie rememberMe = mvc.perform(post("/test/greeting").header(HttpHeaders.AUTHORIZATION, authHeader).param("remember-me", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("remember-me"))
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Hello World!")))
                .andReturn().getResponse().getCookie("remember-me");
        Mockito.verify(userDetailsService).loadUserByUsername("foo");
        Assertions.assertNotNull(rememberMe);
        mvc.perform(post("/logout").cookie(new MockCookie("remember-me", rememberMe.getValue())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().value("remember-me", Matchers.nullValue()))
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.nullValue()));
        // TODO: remember me token is not invalidated on logout, https://github.com/spring-projects/spring-security/issues/10241
//        mvc.perform(post("/test/greeting").cookie(new MockCookie("remember-me", rememberMe.getValue())))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status", Matchers.equalTo(401)));
    }

    @Test
    void formLogin() throws Exception {
        Principal principal = new Principal("Foo", "foo", "{noop}123456", Collections.emptyList(), true, true, true, true);
        Mockito.when(userDetailsService.loadUserByUsername("foo")).thenReturn(principal);
        mvc.perform(post("/login").param("username", "foo").param("password", "123456"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data.principal.name", Matchers.equalTo("Foo")));
        Mockito.verify(userDetailsService).loadUserByUsername("foo");
    }

    @Test
    void formLogin_wrongPassword() throws Exception {
        Principal principal = new Principal("Foo", "foo", "{noop}123456", Collections.emptyList(), true, true, true, true);
        Mockito.when(userDetailsService.loadUserByUsername("foo")).thenReturn(principal);
        mvc.perform(post("/login").param("username", "foo").param("password", "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(401)));
        Mockito.verify(userDetailsService).loadUserByUsername("foo");
    }

    @Test
    void formLogin_userNotFound() throws Exception {
        Mockito.when(userDetailsService.loadUserByUsername("foo")).thenThrow(new UsernameNotFoundException("foo"));
        mvc.perform(post("/login").param("username", "foo").param("password", "12345"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(401)));
        Mockito.verify(userDetailsService).loadUserByUsername("foo");
    }

    @RestController
    public static class TestController {
        @PostMapping("/test/greeting")
        public R<String> greeting() {
            return R.success("Hello World!");
        }
    }
}
