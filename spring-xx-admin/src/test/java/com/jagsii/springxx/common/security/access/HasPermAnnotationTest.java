package com.jagsii.springxx.common.security.access;

import com.jagsii.springxx.WebTests;
import com.jagsii.springxx.common.security.PermGrantedAuthority;
import com.jagsii.springxx.common.security.Principal;
import com.jagsii.springxx.common.security.access.annotation.HasPerm;
import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.config.SecurityConfiguration;
import com.jagsii.springxx.config.WebSecurityConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        SecurityConfiguration.class,
        WebSecurityConfiguration.class,
        HasPermAnnotationTest.TestController.class,
        HasPermAnnotationTest.Test2Controller.class,
})
@ImportAutoConfiguration({
        SecurityAutoConfiguration.class
})
@TestPropertySource(properties = {
        "platform.security.permit-all[0]=/foo/*"
})
class HasPermAnnotationTest extends WebTests {
    @Test
    void methodAuthorized() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("foo:a"));
        Principal principal = new Principal(1L, "foo", "foo", "123456", authorities, true, false, false, false);
        mvc.perform(get("/foo/a").with(user(principal)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("SUCCESS")));
    }

    @Test
    void methodUnauthorized() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("foo:a"));
        Principal principal = new Principal(1L, "foo", "foo", "123456", authorities, true, false, false, false);
        mvc.perform(get("/foo/b").with(user(principal)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    void typeAuthorized() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("foo:c"));
        Principal principal = new Principal(1L, "foo", "foo", "123456", authorities, true, false, false, false);
        mvc.perform(get("/foo/c").with(user(principal)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("SUCCESS")));
    }

    @Test
    void typeUnauthorized() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new PermGrantedAuthority("foo:a"));
        Principal principal = new Principal(1L, "foo", "foo", "123456", authorities, true, false, false, false);
        mvc.perform(get("/foo/c").with(user(principal)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(HttpStatus.FORBIDDEN.value())));
    }

    @RestController
    public static class TestController {
        @HasPerm("foo:a")
        @GetMapping("/foo/a")
        public R<String> a() {
            return R.success("SUCCESS");
        }

        @HasPerm("foo:b")
        @GetMapping("/foo/b")
        public R<String> b() {
            return R.success("SUCCESS");
        }
    }

    @HasPerm("foo:c")
    @RestController
    public static class Test2Controller {
        @GetMapping("/foo/c")
        public R<String> c() {
            return R.success("SUCCESS");
        }
    }
}
