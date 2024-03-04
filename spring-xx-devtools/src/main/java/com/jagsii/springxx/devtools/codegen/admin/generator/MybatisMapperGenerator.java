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
public class MybatisMapperGenerator extends CodeGenerator<EntityMeta> {
    @Override
    @SneakyThrows
    public List<GeneratedFile> generate(EntityMeta entity) {
        Set<String> imports = new HashSet<>();
        imports.add("org.apache.ibatis.annotations.Mapper");
        JavaType baseMapper = entity.getBaseMapper();
        if (baseMapper != null) {
            imports.add(baseMapper.getFullyQualifiedName());
            if (StringUtils.isNotEmpty(baseMapper.getTypeParameters()) && baseMapper.getTypeParameters().contains("{Id}")) {
                imports.add(entity.getPrimaryKey().getJavaType().getFullyQualifiedName());
            }
        }
        String mapperPackageName = entity.getMapperPackageName();
        Map<String, Object> data = new HashMap<>();
        data.put("entity", entity);
        data.put("imports", GeneratorHelper.renderImports(imports, mapperPackageName));
        String source = renderTemplate("mybatis_mapper.ftl", data);
        String dir = GeneratorHelper.packageToPath(mapperPackageName);
        String file = Paths.get(entity.getProject().getRoot(), entity.getProject().getSourceRoot(), dir, entity.getClassName() + "Mapper.java").toString();
        return Collections.singletonList(new GeneratedFile(file, new Formatter().formatSourceAndFixImports(source)));
    }
}
