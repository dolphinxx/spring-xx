package com.jagsii.springxx.devtools.os;

import java.util.Locale;

public class Platform {
    public static final boolean isLinux;
    public static final boolean isMac;
    public static final boolean isWindows;
    static {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        isLinux = osName.startsWith("linux");
        isMac = osName.startsWith("mac");
        isWindows = osName.startsWith("windows");
    }
}
