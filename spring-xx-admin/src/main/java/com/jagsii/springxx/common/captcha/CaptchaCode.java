package com.jagsii.springxx.common.captcha;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaCode implements Serializable {
    private static final long serialVersionUID = 1L;
    private String key;
    private String code;
}
