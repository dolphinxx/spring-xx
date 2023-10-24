package com.jagsii.springxx.common.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileNameInfo {
    private final String basename;
    private final String ext;
    private final String originFilename;

    public FileNameInfo(String originFilename) {
        if (originFilename == null) {
            throw new IllegalArgumentException("originFilename should not be null.");
        }
        this.originFilename = originFilename;
        int pos = originFilename.lastIndexOf(".");
        if (pos > 0 && pos < originFilename.length() - 1) {
            basename = originFilename.substring(0, pos);
            ext = originFilename.substring(pos);
        } else {
            basename = originFilename;
            ext = "";
        }
    }

    public String getExtWithoutDot() {
        return ext.length() == 0 ? ext : "." + ext;
    }
}
