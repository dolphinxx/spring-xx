package com.jagsii.springxx.devtools.codegen.admin.generator;

import com.google.googlejavaformat.java.Formatter;
import com.jagsii.springxx.devtools.codegen.CodeGenerator;
import com.jagsii.springxx.devtools.codegen.GeneratedFile;
import com.jagsii.springxx.devtools.codegen.GeneratorHelper;
import com.jagsii.springxx.devtools.codegen.JavaType;
import com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.*;

@Component
public class ServiceGenerator extends CodeGenerator<EntityMeta> {
    @Override
    @SneakyThrows
    public List<GeneratedFile> generate(EntityMeta entity) {
        return Arrays.asList(generateInterface(entity), generateImpl(entity));
    }

    @SneakyThrows
    GeneratedFile generateInterface(EntityMeta entity) {
        Set<String> imports = new HashSet<>();
        JavaType base = entity.getBaseService();
        if (base != null) {
            imports.add(base.getFullyQualifiedName());
            if (StringUtils.isNotEmpty(base.getTypeParameters()) && base.getTypeParameters().contains("{Id}")) {
                imports.add(entity.getPrimaryKey().getJavaType().getFullyQualifiedName());
            }
        }
        String packageName = entity.getServicePackageName();
        Map<String, Object> data = new HashMap<>();
        data.put("entity", entity);
        data.put("imports", GeneratorHelper.renderImports(imports, packageName));
        String source = renderTemplate("service.ftl", data);
        String dir = GeneratorHelper.packageToPath(packageName);
        String file = Paths.get(entity.getProject().getRoot(), entity.getProject().getSourceRoot(), dir, entity.getClassName() + "Service.java").toString();
        return new GeneratedFile(file, new Formatter().formatSourceAndFixImports(source));
    }

    @SneakyThrows
    GeneratedFile generateImpl(EntityMeta entity) {
        Set<String> imports = new HashSet<>();
        JavaType base = entity.getBaseServiceImpl();
        if (base != null) {
            imports.add(base.getFullyQualifiedName());
            if (StringUtils.isNotEmpty(base.getTypeParameters()) && base.getTypeParameters().contains("{Id}")) {
                imports.add(entity.getPrimaryKey().getJavaType().getFullyQualifiedName());
            }
        }
        if (StringUtils.isNotEmpty(entity.getProject().getServiceImplSubPackage())) {
            imports.add(entity.getServicePackageName() + "." + entity.getClassName() + "Service");
        }
        imports.add("org.springframework.stereotype.Service");
        String packageName = entity.getServicePackageName();
        Map<String, Object> data = new HashMap<>();
        data.put("entity", entity);
        data.put("imports", GeneratorHelper.renderImports(imports, packageName));
        String source = renderTemplate("mybatis_service.ftl", data);
        String dir = GeneratorHelper.packageToPath(packageName);
        String file = Paths.get(entity.getProject().getRoot(), entity.getProject().getSourceRoot(), dir, entity.getClassName() + "MybatisService.java").toString();
        return new GeneratedFile(file, new Formatter().formatSourceAndFixImports(source));
    }
}
