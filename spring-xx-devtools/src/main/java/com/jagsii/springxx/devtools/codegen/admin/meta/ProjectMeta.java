package com.jagsii.springxx.devtools.codegen.admin.meta;

import com.jagsii.springxx.devtools.codegen.JavaType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMeta {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String groupId;
    @Getter
    @Setter
    private String artifact;
    @Getter
    @Setter
    private String packageName;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String catalog;
    @Getter
    @Setter
    private String schema;
    /**
     * The project root dir
     */
    @Getter
    @Setter
    private String root;
    @Getter
    @Setter
    private String sourceRoot = "src/main/java";
    @Getter
    @Setter
    private String resourceRoot = "src/main/resources";
    @Getter
    @Setter
    private String testSourceRoot = "src/test/java";
    @Getter
    @Setter
    private String testResourceRoot = "src/test/resources";
    @Getter
    @Setter
    private String entitySubPackage = "entity";
    @Getter
    @Setter
    private String mybatisMapperSubPackage = "mapper";
    @Getter
    @Setter
    private String serviceSubPackage = "service";
    @Getter
    @Setter
    private String serviceImplSubPackage = "";
    @Getter
    @Setter
    private String controllerSubPackage = "controller";
    @Getter
    @Setter
    private boolean enableValidation = true;
    @Getter
    @Setter
    private DataSourceMeta dataSource;
    @Getter
    @Setter
    private List<ModuleMeta> modules;
    /**
     * The optional super class of the entities. Prefer to the one defined in the module.
     */
    @Getter
    @Setter
    private JavaType baseEntity;
    /**
     * The optional super interface of the mybatis mappers. Prefer to the one defined in the module.
     */
    @Getter
    @Setter
    private JavaType baseMapper;

    @Getter
    @Setter
    private JavaType baseService;
    @Getter
    @Setter
    private JavaType baseServiceImpl;

    @Getter
    @Setter
    private JavaType baseController;

    @Getter
    @Setter
    private Map<String, Object> context = new HashMap<>();

    public ProjectMeta addModule(ModuleMeta module) {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        modules.add(module);
        module.setProject(this);
        return this;
    }
}
