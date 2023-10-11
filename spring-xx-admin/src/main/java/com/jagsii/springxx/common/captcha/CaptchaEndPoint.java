package com.jagsii.springxx.common.captcha;

import com.jagsii.springxx.common.web.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CaptchaEndPoint {
    private final CaptchaProvider<?> captchaProvider;

    /**
     * Generate captcha
     *
     * @return generated captcha data
     */
    @GetMapping("/captcha")
    public R<?> generateCaptcha() {
        String key = UUID.randomUUID().toString();
        return R.success(captchaProvider.generate(key));
    }

    /**
     * Verify user behavior, and return code when verified.
     *
     * @param request {@link HttpServletRequest}
     * @return captcha code if success
     */
    @PostMapping("/captcha/behavior")
    public R<String> verifyBehavior(HttpServletRequest request) {
        String result = captchaProvider.verifyBehavior(request);
        if (result == null) {
            return R.clientError();
        }
        return R.success(result);
    }
}
