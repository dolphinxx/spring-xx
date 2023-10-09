package com.jagsii.springxx.common.security.expression;

import com.jagsii.springxx.common.security.PermGrantedAuthority;
import com.jagsii.springxx.config.SecurityConfiguration;
import com.jagsii.springxx.test.SpringTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Import({
        SecurityConfiguration.class,
        ExtMethodSecurityExpressionTest.FooServiceImpl.class,
})
@ImportAutoConfiguration({
        SecurityAutoConfiguration.class
})
public class ExtMethodSecurityExpressionTest extends SpringTests {
    @Autowired
    FooService fooService;

    @Test
    void hasPermission_denied() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("foo:read"));
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(new Object(), new Object(), authorities);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        Assertions.assertThrows(AccessDeniedException.class, () -> fooService.denied());
    }

    @Test
    void hasPermission() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("foo:read"));
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(new Object(), new Object(), authorities);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        fooService.read();
    }

    @Test
    void hasPerm() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("foo:read"));
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(new Object(), new Object(), authorities);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        fooService.read2();
    }

    public interface FooService {
        void denied();

        void read();

        void read2();
    }

    @Service
    public static class FooServiceImpl implements FooService {
        @Override
        @PreAuthorize("hasPermission(null, 'foo:delete')")
        public void denied() {
            System.out.println("success!");
        }

        @Override
        @PreAuthorize("hasPermission(null, 'foo:read')")
        public void read() {
            System.out.println("success!");
        }

        @Override
        @PreAuthorize("hasPerm('foo:read')")
        public void read2() {
            System.out.println("success!");
        }
    }
}
