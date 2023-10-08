package com.jagsii.springxx.test.hamcrest.number;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class IsNumericalEqual<T> extends BaseMatcher<T> {
    private final Object expectedValue;

    public IsNumericalEqual(T equalArg) {
        expectedValue = equalArg;
    }

    @Override
    public boolean matches(Object actualValue) {
        return areEqual(actualValue, expectedValue);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedValue);
    }

    private static boolean areEqual(Object actual, Object expected) {
        if (actual == null) {
            return expected == null;
        }

        if (!Number.class.isAssignableFrom(actual.getClass()) || !Number.class.isAssignableFrom(expected.getClass())) {
            return false;
        }

        return ((Number) actual).doubleValue() == ((Number) expected).doubleValue();
    }

    public static <T> Matcher<T> numericalEqualTo(T operand) {
        return new IsNumericalEqual<>(operand);
    }
}
