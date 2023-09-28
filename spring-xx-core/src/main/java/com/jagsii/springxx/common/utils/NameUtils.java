package com.jagsii.springxx.common.utils;

public class NameUtils {
    /**
     * Get property name from getter/setter method name.
     */
    public static String methodToProperty(String method) {
        String name;
        if (method.startsWith("is")) {
            name = method.substring(2);
        } else if (method.startsWith("get") || method.startsWith("set")) {
            name = method.substring(3);
        } else {
            throw new IllegalArgumentException("not a getter/setter method: " + method);
        }
        return CaseUtils.pascalToCamel(name);
    }
}
