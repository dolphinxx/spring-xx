package com.jagsii.springxx.devtools.codegen;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneratedFile {
    private String path;
    private String content;

    public GeneratedFile(String path, String content) {
        this.path = path;
        this.content = content;
    }
}
