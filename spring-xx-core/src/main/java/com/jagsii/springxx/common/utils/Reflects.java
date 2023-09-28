package com.jagsii.springxx.common.utils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class Reflects {
    /**
     * Get method name from lambda.<br/>
     * From <a href="https://stackoverflow.com/a/76958693/6592732">Stackoverflow</a>
     */
    public static String methodNameFromLambda(Serializable lambda) {
        try {
            Method m = lambda.getClass().getDeclaredMethod("writeReplace");
            m.setAccessible(true);
            SerializedLambda sl = (SerializedLambda) m.invoke(lambda);
            return sl.getImplMethodName();
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
