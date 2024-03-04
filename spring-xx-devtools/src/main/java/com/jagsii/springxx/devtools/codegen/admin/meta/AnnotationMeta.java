package com.jagsii.springxx.devtools.codegen.admin.meta;

import com.jagsii.springxx.devtools.codegen.JavaType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnotationMeta {
    private JavaType type;
    private String source;

    public AnnotationMeta(JavaType type, String source) {
        this.type = type;
        this.source = source;
    }
}
