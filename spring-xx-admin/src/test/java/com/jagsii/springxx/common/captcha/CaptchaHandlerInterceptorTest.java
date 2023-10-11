package com.jagsii.springxx.common.captcha;

import com.jagsii.springxx.WebTests;
import com.jagsii.springxx.common.web.R;
import com.jagsii.springxx.config.CaptchaConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        CaptchaConfiguration.class,
        CaptchaHandlerInterceptorTest.TestController.class,
})
@TestPropertySource(properties = {
        "platform.security.permit-all[0]=/**",
})
class CaptchaHandlerInterceptorTest extends WebTests {
    @MockBean
    CaptchaProvider<?> captchaProvider;

    @Test
    void alwaysRequiredButInvalid() throws Exception {
        mvc.perform(get("/foo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(400)));
    }

    @Test
    void alwaysRequiredAndValid() throws Exception {
        CaptchaCode mockedCode = new CaptchaCode();
        Mockito.when(captchaProvider.extract(Mockito.any())).thenReturn(mockedCode);
        Mockito.when(captchaProvider.verify(mockedCode)).thenReturn(true);
        mvc.perform(get("/foo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Foo")));
        Mockito.verify(captchaProvider).extract(Mockito.any());
        Mockito.verify(captchaProvider).verify(mockedCode);
    }

    @Test
    void captchaNotRequired() throws Exception {
        mvc.perform(get("/fish"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Fish")))
                .andReturn();
    }

    @Test
    void captchaRequired() throws Exception {
        mvc.perform(get("/fish").sessionAttr("CAPTCHA_REQUIRED_fish", true))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(400)))
                .andReturn();
    }

    @Test
    void noCaptchaAnnotation() throws Exception {
        mvc.perform(get("/foot"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Foot")))
                .andReturn();
    }

    @RestController
    public static class TestController {
        @Captcha("foo")
        @GetMapping("/foo")
        public R<String> foo() {
            return R.success("Foo");
        }

        @Captcha(value = "fish", always = false)
        @GetMapping("/fish")
        public R<String> fish() {
            return R.success("Fish");
        }

        @GetMapping("/foot")
        public R<String> foot() {
            return R.success("Foot");
        }
    }
}