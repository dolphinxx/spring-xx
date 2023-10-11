package com.jagsii.springxx.common.captcha;

import com.jagsii.springxx.WebTests;
import com.jagsii.springxx.config.CaptchaConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {
        "platform.captcha.path-matches[0]=foo /foo",
        "platform.captcha.path-matches[1]=fish? /fish",
})
@Import({
        CaptchaConfiguration.class,
})
class CaptchaFilterTest extends WebTests {
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
                .andExpect(jsonPath("$.status", Matchers.equalTo(401)));
        Mockito.verify(captchaProvider).extract(Mockito.any());
        Mockito.verify(captchaProvider).verify(mockedCode);
    }

    @Test
    void captchaNotRequired() throws Exception {
        mvc.perform(get("/fish"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.equalTo(401)))
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
}