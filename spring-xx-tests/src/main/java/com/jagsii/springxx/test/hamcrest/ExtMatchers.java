package com.jagsii.springxx.test.hamcrest;

import com.jagsii.springxx.test.hamcrest.number.IsNumericalEqual;
import org.hamcrest.Matcher;

public class ExtMatchers {
    public static <T> Matcher<T> numericalEqualTo(T operand) {
        return IsNumericalEqual.numericalEqualTo(operand);
    }
}
