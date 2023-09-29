package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.utils.CaseUtils;
import com.jagsii.springxx.mybatis.annotations.Column;
import com.jagsii.springxx.mybatis.annotations.Id;
import com.jagsii.springxx.mybatis.annotations.Ignore;
import com.jagsii.springxx.mybatis.annotations.Table;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class SqlBuilderHelper {

    private static final ConcurrentMap<String, EntityTableInfo> tableInfoMap = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> sqlMap = new ConcurrentHashMap<>();

    /**
     * Get the entity type.<br/>
     * Copied from <a href="https://github.com/mybatis/mybatis-3/blob/543e1454c8ef6c33fe5bf25b6549cc15e2f33cba/src/test/java/org/apache/ibatis/submitted/sqlprovider/OurSqlBuilder.java#L266C3-L281C4">OurSqlBuilder.java</a>
     */
    public static Class<?> getEntityClass(ProviderContext providerContext) {
//        Method mapperMethod = providerContext.getMapperMethod();
//        Class<?> declaringClass = mapperMethod.getDeclaringClass();
        Class<?> mapperClass = providerContext.getMapperType();
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
//                if (t.getRawType() == declaringClass || mapperClass.isAssignableFrom((Class<?>) t.getRawType())) {
                return (Class<?>) t.getActualTypeArguments()[0];
//                }
            }
        }
        throw new RuntimeException("The interface [" + mapperClass.getCanonicalName() + "] must specify a generic type.");
    }

    public static EntityTableInfo getTableInfo(ProviderContext context) {
        Class<?> entityClass = SqlBuilderHelper.getEntityClass(context);
        return tableInfoMap.computeIfAbsent(entityClass.getName(), key -> SqlBuilderHelper.getTableInfo(entityClass));
    }

    public static EntityTableInfo getTableInfo(Class<?> entityClass) {
        EntityTableInfo info = new EntityTableInfo();

        Table tableAnno = entityClass.getAnnotation(Table.class);
        if (tableAnno != null && tableAnno.name() != null && tableAnno.name().length() > 0) {
            info.setTableName(tableAnno.name());
            if (tableAnno.schema() != null && tableAnno.schema().length() > 0) {
                info.setTableSchema(tableAnno.schema());
            }
            if (tableAnno.catalog() != null && tableAnno.catalog().length() > 0) {
                info.setTableCatalog(tableAnno.catalog());
            }
        } else {
            info.setTableName(CaseUtils.pascalToSnake(entityClass.getSimpleName()));
        }

        Field[] fields = entityClass.getDeclaredFields();
        Map<String, String> columnMap = new LinkedHashMap<>();
        for (Field field : fields) {
            String columnName = getColumnName(field);
            if (StringUtils.hasText(columnName)) {
                columnMap.put(columnName, field.getName());
                if (field.getAnnotation(Id.class) != null) {
                    info.setId(columnName);
                }
            }
        }
        if (columnMap.size() == 0) {
            throw new RuntimeException("There is no field in the class [" + entityClass.getCanonicalName()
                    + "] that specifies the @BaseMapper.Column annotation.");
        }
        if(info.getId() == null) {
            info.setId("id");
        }
        info.setColumns(columnMap);
        return info;
    }

    private static String getColumnName(Field field) {
        if (field.getAnnotation(Ignore.class) != null) {
            return null;
        }
        Column c = field.getAnnotation(Column.class);
        if (c != null && c.name() != null && c.name().length() > 0) {
            return c.name();
        }
        return CaseUtils.pascalToSnake(field.getName());
    }

    public static String getMapperId(ProviderContext context) {
        return context.getMapperType().getName() + "#" + context.getMapperMethod().getName();
    }

    public static String buildSql(ProviderContext context, Function<EntityTableInfo, String> builder) {
        String mapperId = getMapperId(context);
        return sqlMap.computeIfAbsent(mapperId, key -> {
            EntityTableInfo tableInfo = getTableInfo(context);
            return builder.apply(tableInfo);
        });
    }

    public static String buildTableName(EntityTableInfo tableInfo) {
        StringBuilder sb = new StringBuilder();
        if (tableInfo.getTableCatalog() != null) {
            sb.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(tableInfo.getTableCatalog()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(".");
        }
        if (tableInfo.getTableSchema() != null) {
            sb.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(tableInfo.getTableSchema()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(".");
        }
        sb.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(tableInfo.getTableName()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR);
        return sb.toString();
    }
}
