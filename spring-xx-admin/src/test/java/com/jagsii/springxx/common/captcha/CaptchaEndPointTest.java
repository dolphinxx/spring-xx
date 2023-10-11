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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "platform.security.permit-all[0]=/**")
@Import({
        CaptchaEndPoint.class,
        CaptchaConfiguration.class,
        CaptchaEndPointTest.TestController.class,
})
class CaptchaEndPointTest extends WebTests {
    @MockBean
    CaptchaProvider<?> captchaProvider;

    @Test
    void withoutBehavior() throws Exception {
        String captchaKey = "a-b-c";
        String verifiedCode = captchaKey + ":" + "123456";
        //noinspection rawtypes
        Mockito.when(((CaptchaProvider) captchaProvider).generate(Mockito.any())).thenReturn(verifiedCode);
        mvc.perform(get("/captcha"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo(verifiedCode)));
        Mockito.verify(captchaProvider).generate(Mockito.any());

        Mockito.when(captchaProvider.extract(Mockito.argThat(arg -> arg.getParameter("captcha").equals(verifiedCode)))).thenReturn(new CaptchaCode(captchaKey, "123456"));
        Mockito.when(captchaProvider.verify(Mockito.argThat(arg -> arg.getKey().equals(captchaKey) && arg.getCode().equals("123456")))).thenReturn(true);
        mvc.perform(post("/foo").param("captcha", verifiedCode))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Success")));
        Mockito.verify(captchaProvider).extract(Mockito.any());
        Mockito.verify(captchaProvider).verify(Mockito.any());
    }

    @Test
    void withBehavior() throws Exception {
        String captchaKey = "a-b-c";
        //noinspection rawtypes
        Mockito.when(((CaptchaProvider) captchaProvider).generate(Mockito.any())).thenReturn(Collections.singletonMap("key", captchaKey));
        mvc.perform(get("/captcha"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data.key", Matchers.equalTo(captchaKey)));
        Mockito.verify(captchaProvider).generate(Mockito.any());

        String verifiedCode = captchaKey + ":" + "123456";
        Mockito.when(captchaProvider.verifyBehavior(Mockito.argThat(arg -> arg.getParameter("key").equals(captchaKey) && arg.getParameter("data").equals("1234567890")))).thenReturn(verifiedCode);
        mvc.perform(post("/captcha/behavior").param("key", captchaKey).param("data", "1234567890"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo(verifiedCode)));
        Mockito.verify(captchaProvider).verifyBehavior(Mockito.any());

        Mockito.when(captchaProvider.extract(Mockito.argThat(arg -> arg.getParameter("captcha").equals(verifiedCode)))).thenReturn(new CaptchaCode(captchaKey, "123456"));
        Mockito.when(captchaProvider.verify(Mockito.argThat(arg -> arg.getKey().equals(captchaKey) && arg.getCode().equals("123456")))).thenReturn(true);
        mvc.perform(post("/foo").param("captcha", verifiedCode))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(isOk)
                .andExpect(jsonPath("$.data", Matchers.equalTo("Success")));
        Mockito.verify(captchaProvider).extract(Mockito.any());
        Mockito.verify(captchaProvider).verify(Mockito.any());
    }

    @RestController
    public static class TestController {
        @Captcha("foo")
        @PostMapping("/foo")
        public R<String> foo() {
            return R.success("Success");
        }
    }
}