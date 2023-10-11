package com.jagsii.springxx.common.captcha.ta;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.ImageTransform;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.generator.impl.MultiImageCaptchaGenerator;
import cloud.tianai.captcha.generator.impl.transform.Base64ImageTransform;
import cloud.tianai.captcha.resource.ImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.impl.DefaultImageCaptchaResourceManager;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import cloud.tianai.captcha.validator.impl.BasicCaptchaTrackValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jagsii.springxx.common.captcha.CaptchaCode;
import com.jagsii.springxx.common.captcha.CaptchaProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TianAiCaptchaProvider implements CaptchaProvider<ImageCaptchaInfo> {
    private final ImageCaptchaResourceManager imageCaptchaResourceManager = new DefaultImageCaptchaResourceManager();
    private final ImageTransform imageTransform = new Base64ImageTransform();
    private final ImageCaptchaGenerator captchaGenerator = new MultiImageCaptchaGenerator(imageCaptchaResourceManager, imageTransform).init(true);
    private final ImageCaptchaValidator captchaValidator = new BasicCaptchaTrackValidator();
    private final Cache<String, Object> cache = CacheBuilder.newBuilder().maximumSize(512).expireAfterWrite(5, TimeUnit.MINUTES).build();

    private final ObjectMapper objectMapper;

    public TianAiCaptchaProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ImageCaptchaInfo generate(String key) {
        ImageCaptchaInfo imageCaptchaInfo = captchaGenerator.generateCaptchaImage(CaptchaTypeConstant.SLIDER);
        cache.put(key, captchaValidator.generateImageCaptchaValidData(imageCaptchaInfo));
        imageCaptchaInfo.getData().putViewData("key", key);
        return imageCaptchaInfo;
    }

    @Override
    public boolean verify(CaptchaCode value) {
        if (value == null) {
            return false;
        }
        String key = value.getKey();
        String validCode = (String) cache.getIfPresent(key);
        if (validCode == null) {
            return false;
        }
        cache.invalidate(key);
        return Objects.equals(value.getCode(), validCode);
    }

    @Override
    public CaptchaCode extract(HttpServletRequest request) {
        try {
            String captcha = request.getParameter("captcha");
            if (captcha == null) {
                return null;
            }
            int index = captcha.indexOf(":");
            if (index == -1) {
                return null;
            }
            return new CaptchaCode(captcha.substring(0, index), captcha.substring(index + 1));
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public String verifyBehavior(HttpServletRequest request) {
        try {
            String key = request.getParameter("key");
            String data = request.getParameter("data");
            if (key == null || data == null) {
                return null;
            }
            Object validateInfo = cache.getIfPresent(key);
            if (validateInfo == null || validateInfo.getClass().equals(String.class)) {
                return null;
            }
            ImageCaptchaTrack trackData = objectMapper.readValue(Base64.getDecoder().decode(data), ImageCaptchaTrack.class);
            //noinspection unchecked
            if (captchaValidator.valid(trackData, (Map<String, Object>) validateInfo).isSuccess()) {
                String token = UUID.randomUUID().toString();
                cache.put(key, token);
                return token;
            }
            cache.invalidate(key);
        } catch (Exception ignore) {
        }
        return null;
    }
}
