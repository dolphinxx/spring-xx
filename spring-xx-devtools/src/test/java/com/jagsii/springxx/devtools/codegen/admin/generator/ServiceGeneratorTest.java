package com.jagsii.springxx.devtools.codegen.admin.generator;

import com.jagsii.springxx.test.SpringTests;
import com.jagsii.springxx.devtools.codegen.FreemarkerConfig;
import com.jagsii.springxx.devtools.codegen.GeneratedFile;
import com.jagsii.springxx.devtools.codegen.JavaType;
import com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta;
import com.jagsii.springxx.devtools.codegen.admin.meta.FieldMeta;
import com.jagsii.springxx.devtools.codegen.admin.meta.ModuleMeta;
import com.jagsii.springxx.devtools.codegen.admin.meta.ProjectMeta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
        FreemarkerConfig.class,
        ServiceGenerator.class,
})
class ServiceGeneratorTest extends SpringTests {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ServiceGenerator generator;

    @Test
    void generate() throws Exception {
        ProjectMeta project = new ProjectMeta();
        project.setName("Foo");
        project.setGroupId("com.example");
        project.setArtifact("foo");
        project.setPackageName("com.example.foo");
        project.setDescription("Foo project.");
        project.setRoot("/workspace/foo");

        ModuleMeta module = new ModuleMeta();
        module.setName("system");
        module.setPackagePrefix("modules");
        project.addModule(module);

        EntityMeta entity = new EntityMeta();
        entity.setTableName("user");
        JavaType baseService = new JavaType("com.example.common.BaseService");
        baseService.setTypeParameters("{Entity}, {Id}");
        entity.setBaseService(baseService);
        JavaType baseServiceImpl = new JavaType("com.example.common.BaseMybatisService");
        baseServiceImpl.setTypeParameters("{Entity}, {Id}");
        entity.setBaseServiceImpl(baseServiceImpl);

        FieldMeta field;

        field = new FieldMeta();
        field.setColumnName("id");
        field.setJavaType(new JavaType(Long.class));
        field.setJdbcType(Types.BIGINT);
        field.setIdentity(true);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("name");
        field.setJavaType(new JavaType(String.class));
        field.setJdbcType(Types.VARCHAR);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("username");
        field.setJavaType(new JavaType(String.class));
        field.setJdbcType(Types.VARCHAR);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("password");
        field.setJavaType(new JavaType(String.class));
        field.setJdbcType(Types.VARCHAR);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("birth");
        field.setJavaType(new JavaType(LocalDate.class));
        field.setJdbcType(Types.DATE);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("status");
        field.setJavaType(new JavaType(Integer.class));
        field.setJdbcType(Types.TINYINT);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("remark");
        field.setJavaType(new JavaType(String.class));
        field.setJdbcType(Types.VARCHAR);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("create_time");
        field.setJavaType(new JavaType(LocalDateTime.class));
        field.setJdbcType(Types.TIMESTAMP);
        entity.addField(field);

        field = new FieldMeta();
        field.setColumnName("update_time");
        field.setJavaType(new JavaType(LocalDateTime.class));
        field.setJdbcType(Types.TIMESTAMP);
        entity.addField(field);

        module.addEntity(entity);
        List<GeneratedFile> actualFiles = generator.generate(entity);
        {
            GeneratedFile file = actualFiles.get(0);
            assertThat(file.getPath()).isEqualTo("\\workspace\\foo\\src\\main\\java\\com\\example\\foo\\modules\\system\\service\\UserService.java");
            try (InputStream expected = new DefaultResourceLoader().getResource("classpath:/data/UserService.java.txt").getInputStream()) {
                assertThat(new ByteArrayInputStream(file.getContent().getBytes(StandardCharsets.UTF_8))).hasSameContentAs(expected);
            }
        }
        {
            GeneratedFile file = actualFiles.get(1);
            assertThat(file.getPath()).isEqualTo("\\workspace\\foo\\src\\main\\java\\com\\example\\foo\\modules\\system\\service\\UserMybatisService.java");
            try (InputStream expected = new DefaultResourceLoader().getResource("classpath:/data/UserMybatisService.java.txt").getInputStream()) {
                assertThat(new ByteArrayInputStream(file.getContent().getBytes(StandardCharsets.UTF_8))).hasSameContentAs(expected);
            }
        }
    }
}