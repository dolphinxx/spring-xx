package com.jagsii.springxx.devtools.codegen.admin.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jagsii.springxx.devtools.codegen.JavaType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleMeta {
    @Getter
    @Setter
    private String name;
    @Setter
    private String catalog;
    @Setter
    private String schema;
    /**
     * An optional package prefix.
     */
    @Getter
    @Setter
    private String packagePrefix;
    @Getter
    @Setter
    private List<EntityMeta> entities;
    @Getter
    @Setter
    @JsonIgnore
    private ProjectMeta project;

    /**
     * The optional super class of the entities. Prefer to the one defined in the entity.
     */
    @Setter
    private JavaType baseEntity;
    /**
     * The optional super interface of the mybatis mappers. Prefer to the one defined in the entity.
     */
    @Setter
    private JavaType baseMapper;

    @Setter
    private JavaType baseService;
    @Setter
    private JavaType baseServiceImpl;

    @Setter
    private JavaType baseController;

    private String packageName;

    @Getter
    @Setter
    private Map<String, Object> context = new HashMap<>();

    public ModuleMeta addEntity(EntityMeta entity) {
        if (entities == null) {
            entities = new ArrayList<>();
        }
        entities.add(entity);
        entity.setModule(this);
        return this;
    }

    public JavaType getBaseEntity() {
        if (this.baseEntity != null) {
            return this.baseEntity;
        }
        return project.getBaseEntity();
    }

    public JavaType getBaseMapper() {
        if (this.baseMapper != null) {
            return this.baseMapper;
        }
        return project.getBaseMapper();
    }

    public JavaType getBaseService() {
        if (this.baseService != null) {
            return this.baseService;
        }
        return project.getBaseService();
    }

    public JavaType getBaseServiceImpl() {
        if (this.baseServiceImpl != null) {
            return this.baseServiceImpl;
        }
        return project.getBaseServiceImpl();
    }

    public JavaType getBaseController() {
        if (this.baseController != null) {
            return this.baseController;
        }
        return project.getBaseController();
    }

    public String getPackageName() {
        if (packageName == null) {
            StringBuilder buff = new StringBuilder();
            buff.append(project.getPackageName());
            if (StringUtils.isNotEmpty(packagePrefix)) {
                buff.append(".").append(packagePrefix);
            }
            buff.append(".").append(name);
            packageName = buff.toString();
        }
        return packageName;
    }

    public String getCatalog() {
        if(StringUtils.isNotEmpty(catalog)) {
            return catalog;
        }
        return project.getCatalog();
    }

    public String getSchema() {
        if(StringUtils.isNotEmpty(schema)) {
            return schema;
        }
        return project.getSchema();
    }
}
