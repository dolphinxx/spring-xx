package com.jagsii.springxx.devtools.codegen.admin.generator;

import com.google.googlejavaformat.java.Formatter;
import com.jagsii.springxx.devtools.codegen.CodeGenerator;
import com.jagsii.springxx.devtools.codegen.GeneratedFile;
import com.jagsii.springxx.devtools.codegen.GeneratorHelper;
import com.jagsii.springxx.devtools.codegen.JavaType;
import com.jagsii.springxx.devtools.codegen.admin.meta.AnnotationMeta;
import com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta;
import com.jagsii.springxx.devtools.codegen.admin.meta.FieldMeta;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.*;

@Component
public class EntityGenerator extends CodeGenerator<EntityMeta> {
    @SneakyThrows
    public List<GeneratedFile> generate(EntityMeta entity) {
        if (entity.getProject().isEnableValidation()) {
            initValidators(entity.getFields());
        }
        Set<String> imports = new HashSet<>();
        imports.add("lombok.Getter");
        imports.add("lombok.Setter");
        JavaType baseEntity = entity.getBaseEntity();
        if (baseEntity != null) {
            imports.add(baseEntity.getFullyQualifiedName());
        }
        for (FieldMeta field : entity.getFields()) {
            imports.add(field.getJavaType().getFullyQualifiedName());
        }
        String entityPackageName = entity.getEntityPackageName();
        Map<String, Object> data = new HashMap<>();
        data.put("entity", entity);
        data.put("imports", GeneratorHelper.renderImports(imports, entityPackageName));
        String source = renderTemplate("entity.ftl", data);
        String dir = GeneratorHelper.packageToPath(entityPackageName);
        String file = Paths.get(entity.getProject().getRoot(), entity.getProject().getSourceRoot(), dir, entity.getClassName() + ".java").toString();
        return Collections.singletonList(new GeneratedFile(file, new Formatter().formatSourceAndFixImports(source)));
    }

    private void initValidators(List<FieldMeta> fields) {
        for (FieldMeta field : fields) {
            initValidator(field);
        }
    }

    private void initValidator(FieldMeta field) {
        List<AnnotationMeta> validators = new ArrayList<>();
        if (!field.isNullable()) {
            if (field.getJavaType().getFullyQualifiedName().equals(String.class.getName()) && field.getLength() > 0) {
                // @NotBlank
                validators.add(new AnnotationMeta(new JavaType("javax.validation.constraints.NotBlank"), "@NotBlank"));
                // @Size(max = ?)
                validators.add(new AnnotationMeta(new JavaType("javax.validation.constraints.Size"), "@Size(max = " + field.getLength() + ")"));
            }
        }
        field.setValidators(validators);
    }
}
