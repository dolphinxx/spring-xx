package com.jagsii.springxx.devtools.codegen.admin.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jagsii.springxx.common.utils.CaseUtils;
import com.jagsii.springxx.devtools.codegen.JavaType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityMeta {
    @Getter
    @Setter
    private String tableName;
    @Setter
    private String catalog;
    @Setter
    private String schema;
    @Setter
    private String className;
    @Setter
    private String variableName;
    @Getter
    @Setter
    private List<FieldMeta> fields;
    @Getter
    @Setter
    @JsonIgnore
    private ModuleMeta module;
    @Setter
    private JavaType baseEntity;
    @Setter
    private JavaType baseMapper;
    @Setter
    private JavaType baseService;
    @Setter
    private JavaType baseServiceImpl;
    @Setter
    private JavaType baseController;
    @JsonIgnore
    private FieldMeta primaryKey;
    @Getter
    @Setter
    private String remarks;

    @Getter
    @Setter
    private Map<String, Object> context = new HashMap<>();

    public EntityMeta addField(FieldMeta field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
        field.setEntity(this);
        return this;
    }

    public String getEntityPackageName() {
        return module.getPackageName() + "." + getProject().getEntitySubPackage();
    }

    public String getMapperPackageName() {
        return module.getPackageName() + "." + getProject().getMybatisMapperSubPackage();
    }

    public String getServicePackageName() {
        return module.getPackageName() + "." + getProject().getServiceSubPackage();
    }

    public String getServiceImplPackageName() {
        String servicePackage = getServicePackageName();
        return StringUtils.isEmpty(getProject().getServiceImplSubPackage()) ? servicePackage : servicePackage + "." + getProject().getServiceImplSubPackage();
    }

    public String getControllerPackageName() {
        return module.getPackageName() + "." + getProject().getControllerSubPackage();
    }

    public String getClassName() {
        if (className == null) {
            className = CaseUtils.snakeToPascal(tableName);
        }
        return className;
    }

    public String getVariableName() {
        if (variableName == null) {
            variableName = CaseUtils.snakeToCamel(tableName);
        }
        return variableName;
    }

    public JavaType getBaseEntity() {
        if (this.baseEntity != null) {
            return this.baseEntity;
        }
        return this.module.getBaseEntity();
    }

    public JavaType getBaseMapper() {
        if (this.baseMapper != null) {
            return this.baseMapper;
        }
        return this.module.getBaseMapper();
    }

    public JavaType getBaseService() {
        if (this.baseService != null) {
            return this.baseService;
        }
        return this.module.getBaseService();
    }

    public JavaType getBaseServiceImpl() {
        if (this.baseServiceImpl != null) {
            return this.baseServiceImpl;
        }
        return this.module.getBaseServiceImpl();
    }

    public JavaType getBaseController() {
        if (this.baseController != null) {
            return this.baseController;
        }
        return this.module.getBaseController();
    }

    public ProjectMeta getProject() {
        return module.getProject();
    }

    public String getCatalog() {
        if (StringUtils.isNotEmpty(catalog)) {
            return catalog;
        }
        return module.getCatalog();
    }

    public String getSchema() {
        if (StringUtils.isNotEmpty(schema)) {
            return schema;
        }
        return module.getSchema();
    }

    public String getFullyQualifiedTableName() {
        StringBuilder result = new StringBuilder();
        String _catalog = getCatalog();
        String _schema = getSchema();
        if (StringUtils.isNotEmpty(_catalog)) {
            result.append("`").append(_catalog).append("`.");
        }
        if (StringUtils.isNotEmpty(_schema)) {
            result.append("`").append(_schema).append("`.");
        }
        result.append("`").append(tableName).append("`");
        return result.toString();
    }

    public FieldMeta getPrimaryKey() {
        if (primaryKey == null) {
            for (FieldMeta field : fields) {
                if (field.isIdentity()) {
                    primaryKey = field;
                    break;
                }
            }
        }
        return primaryKey;
    }
}
