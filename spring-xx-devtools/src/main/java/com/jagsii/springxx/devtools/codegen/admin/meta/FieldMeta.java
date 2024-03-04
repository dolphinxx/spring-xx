package com.jagsii.springxx.devtools.codegen.admin.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jagsii.springxx.common.utils.CaseUtils;
import com.jagsii.springxx.devtools.codegen.JavaType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldMeta {
    @Getter
    @Setter
    private String columnName;
    @Setter
    private String propertyName;
    @Getter
    @Setter
    private int jdbcType;
    @Getter
    @Setter
    private String jdbcTypeName;
    @Getter
    @Setter
    private JavaType javaType;
    @Getter
    @Setter
    @JsonIgnore
    private EntityMeta entity;
    @Getter
    @Setter
    protected boolean nullable;
    @Getter
    @Setter
    protected int length;
    @Getter
    @Setter
    protected int scale;
    @Getter
    @Setter
    private boolean identity;
    @Getter
    @Setter
    private String remarks;
    @Getter
    @Setter
    private String defaultValue;
    @Getter
    @Setter
    private boolean autoIncrement;
    /**
     * Validator annotations
     */
    @Getter
    @Setter
    private List<AnnotationMeta> validators;

    @Getter
    @Setter
    private Map<String, Object> context = new HashMap<>();

    public String getPropertyName() {
        if (propertyName == null) {
            propertyName = CaseUtils.snakeToCamel(columnName);
        }
        return propertyName;
    }
}
