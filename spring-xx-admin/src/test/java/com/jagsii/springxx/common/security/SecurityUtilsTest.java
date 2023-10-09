package com.jagsii.springxx.common.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SecurityUtilsTest {

    @Test
    void generateRandomPassword() {
        String actual = SecurityUtils.generateRandomPassword(18);
        System.out.println(actual);
        int lowerCaseCount = 0;
        int upperCaseCount = 0;
        int numberCount = 0;
        int specialCharCount = 0;
        for (char c : actual.toCharArray()) {
            if (c >= 97 && c <= 122) {
                lowerCaseCount++;
                continue;
            }
            if (c >= 65 && c <= 90) {
                upperCaseCount++;
                continue;
            }
            if (c >= 48 && c <= 57) {
                numberCount++;
                continue;
            }
            if (c >= 35 && c <= 38) {
                specialCharCount++;
                continue;
            }
            throw new RuntimeException("unexpected char: " + c);
        }
        Assertions.assertEquals(6, lowerCaseCount, "lower case count");
        Assertions.assertEquals(4, upperCaseCount, "upper case count");
        Assertions.assertEquals(4, numberCount, "number count");
        Assertions.assertEquals(4, specialCharCount, "special char count");
    }
}