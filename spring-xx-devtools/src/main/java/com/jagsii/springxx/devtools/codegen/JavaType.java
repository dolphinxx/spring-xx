package com.jagsii.springxx.devtools.codegen;

import com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta;
import com.jagsii.springxx.devtools.codegen.admin.meta.FieldMeta;
import lombok.Getter;
import lombok.Setter;

@Getter
public class JavaType {
    private final String packageName;
    private final String className;
    private final String fullyQualifiedName;
    /**
     * in the format: SomeType, {Entity}, {Id}
     */
    @Setter
    private String typeParameters;

    public JavaType(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
        this.fullyQualifiedName = packageName + "." + className;
    }

    public JavaType(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.packageName = fullyQualifiedName.substring(0, fullyQualifiedName.lastIndexOf("."));
        this.className = fullyQualifiedName.substring(this.packageName.length() + 1);
    }

    public JavaType(Class<?> clazz) {
        this(clazz.getPackage().getName(), clazz.getSimpleName());
    }

    public String renderTypeParameters(EntityMeta entity) {
        String result = typeParameters.replace("{Entity}", entity.getClassName());
        FieldMeta id = entity.getPrimaryKey();
        if (id != null) {
            result = result.replace("{Id}", id.getJavaType().className);
        }
        return result;
    }
}
