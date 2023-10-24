package com.jagsii.springxx.common.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UploadedFile {
    private String key;
    private String url;
}
