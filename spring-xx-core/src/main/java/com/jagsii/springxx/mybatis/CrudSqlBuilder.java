package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.pagination.PageRequest;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

@SuppressWarnings("unused")
public class CrudSqlBuilder {

    public static String buildInsert(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("INSERT INTO ");
            sqlBuffer.append(SqlBuilderHelper.buildTableName(tableInfo));
            sqlBuffer.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">");
            for (String column : tableInfo.getColumns().keySet()) {
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(column).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(", ");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \">");
            for (String field : tableInfo.getColumns().values()) {
                sqlBuffer.append("#{").append(field).append("}, ");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildInsertBatch(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("INSERT INTO ");
            sqlBuffer.append(SqlBuilderHelper.buildTableName(tableInfo));
            sqlBuffer.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">");
            for (String column : tableInfo.getColumns().keySet()) {
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(column).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(", ");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("VALUES ");
            sqlBuffer.append("<foreach collection=\"list\" item=\"e\" open=\"(\" separator=\"), (\"  close=\")\">");
            sqlBuffer.append("<trim suffixOverrides=\", \">");
            for (String field : tableInfo.getColumns().values()) {
                sqlBuffer.append("#{e.").append(field).append("}, ");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("</foreach>");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildInsertSelective(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("INSERT INTO ");
            sqlBuffer.append(SqlBuilderHelper.buildTableName(tableInfo));
            sqlBuffer.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">");
            for (Map.Entry<String, String> entry : tableInfo.getColumns().entrySet()) {
                sqlBuffer.append("<if test=\"").append(entry.getValue()).append(" != null\">");
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(entry.getKey()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(", ");
                sqlBuffer.append("</if>");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \">");
            for (String field : tableInfo.getColumns().values()) {
                sqlBuffer.append("<if test=\"").append(field).append(" != null\">");
                sqlBuffer.append("#{").append(field).append("}, ");
                sqlBuffer.append("</if>");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildUpdateByPrimaryKey(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("UPDATE ");
            sqlBuffer.append(SqlBuilderHelper.buildTableName(tableInfo));
            sqlBuffer.append("<set>");
            for (Map.Entry<String, String> entry : tableInfo.getColumns().entrySet()) {
                if (entry.getKey().equals(tableInfo.getId())) {
                    continue;
                }
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(entry.getKey()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(" = #{").append(entry.getValue()).append("}, ");
            }
            sqlBuffer.append("</set>");
            sqlBuffer.append("WHERE ");
            sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(tableInfo.getId()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR);
            sqlBuffer.append(" = #{").append(tableInfo.getColumns().get(tableInfo.getId())).append("}");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildUpdateByPrimaryKeySelective(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("UPDATE ");
            sqlBuffer.append(SqlBuilderHelper.buildTableName(tableInfo));
            sqlBuffer.append("<set>");
            for (Map.Entry<String, String> entry : tableInfo.getColumns().entrySet()) {
                if (entry.getKey().equals(tableInfo.getId())) {
                    continue;
                }
                sqlBuffer.append("<if test=\"").append(entry.getValue()).append(" != null\">");
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(entry.getKey()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(" = #{").append(entry.getValue()).append("}, ");
                sqlBuffer.append("</if>");
            }
            sqlBuffer.append("</set>");
            sqlBuffer.append("WHERE ");
            sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(tableInfo.getId()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR);
            sqlBuffer.append(" = #{").append(tableInfo.getColumns().get(tableInfo.getId())).append("}");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildUpsert(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("INSERT INTO ");
            sqlBuffer.append(SqlBuilderHelper.buildTableName(tableInfo));
            sqlBuffer.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">");
            for (String column : tableInfo.getColumns().keySet()) {
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(column).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(", ");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \">");
            for (String field : tableInfo.getColumns().values()) {
                sqlBuffer.append("#{").append(field).append("}, ");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append(" ON DUPLICATE KEY UPDATE ");
            sqlBuffer.append("<trim suffixOverrides=\", \">");
            for (Map.Entry<String, String> entry : tableInfo.getColumns().entrySet()) {
                if (entry.getKey().equals(tableInfo.getId())) {
                    continue;
                }
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(entry.getKey()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(" = #{").append(entry.getValue()).append("}, ");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildUpsertSelective(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("INSERT INTO ");
            sqlBuffer.append(SqlBuilderHelper.buildTableName(tableInfo));
            sqlBuffer.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">");
            for (Map.Entry<String, String> entry : tableInfo.getColumns().entrySet()) {
                sqlBuffer.append("<if test=\"").append(entry.getValue()).append(" != null\">");
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(entry.getKey()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(", ");
                sqlBuffer.append("</if>");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \">");
            for (String field : tableInfo.getColumns().values()) {
                sqlBuffer.append("<if test=\"").append(field).append(" != null\">");
                sqlBuffer.append("#{").append(field).append("}, ");
                sqlBuffer.append("</if>");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append(" ON DUPLICATE KEY UPDATE ");
            sqlBuffer.append("<trim suffixOverrides=\", \">");
            for (Map.Entry<String, String> entry : tableInfo.getColumns().entrySet()) {
                if (entry.getKey().equals(tableInfo.getId())) {
                    continue;
                }
                sqlBuffer.append("<if test=\"").append(entry.getValue()).append(" != null\">");
                sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(entry.getKey()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(" = #{").append(entry.getValue()).append("}, ");
                sqlBuffer.append("</if>");
            }
            sqlBuffer.append("</trim>");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildDeleteByPrimaryKey(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> "DELETE FROM " + SqlBuilderHelper.buildTableName(tableInfo) + " WHERE " + SqlConstant.IDENTIFIER_ESCAPE_CHAR + tableInfo.getId() + SqlConstant.IDENTIFIER_ESCAPE_CHAR + " = " + "#{" + tableInfo.getColumns().get(tableInfo.getId()) + "}");
    }

    public static String buildDeleteByPrimaryKeys(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> {
            StringBuilder sqlBuffer = new StringBuilder();
            sqlBuffer.append("<script>");
            sqlBuffer.append("DELETE FROM ").append(SqlBuilderHelper.buildTableName(tableInfo)).append(" WHERE ");
            sqlBuffer.append(SqlConstant.IDENTIFIER_ESCAPE_CHAR).append(tableInfo.getId()).append(SqlConstant.IDENTIFIER_ESCAPE_CHAR);
            sqlBuffer.append(" IN <foreach collection=\"list\" item=\"e\" open=\"(\" separator=\", \"  close=\")\">#{e}</foreach>");
            sqlBuffer.append("</script>");
            return sqlBuffer.toString();
        });
    }

    public static String buildSelectByPrimaryKey(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> "SELECT * FROM " + SqlBuilderHelper.buildTableName(tableInfo) + " WHERE " + SqlConstant.IDENTIFIER_ESCAPE_CHAR + tableInfo.getId() + SqlConstant.IDENTIFIER_ESCAPE_CHAR + " = #{" + tableInfo.getColumns().get(tableInfo.getId()) + "}");
    }

    public static String buildExistsByPrimaryKey(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> "SELECT COUNT(0) FROM " + SqlBuilderHelper.buildTableName(tableInfo) + " WHERE " + SqlConstant.IDENTIFIER_ESCAPE_CHAR + tableInfo.getId() + SqlConstant.IDENTIFIER_ESCAPE_CHAR + " = #{" + tableInfo.getColumns().get(tableInfo.getId()) + "} LIMIT 1");
    }

    public static String buildSelectOneByExample(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> _buildSelectByExample(SqlBuilderHelper.getEntityClass(context), tableInfo, false, true, null));
    }

    public static String buildSelectByExample(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> _buildSelectByExample(SqlBuilderHelper.getEntityClass(context), tableInfo, false, false, null));
    }

    public static String buildSelectByExampleWithPage(ProviderContext context, Map<String, Object> params) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> _buildSelectByExample(SqlBuilderHelper.getEntityClass(context), tableInfo, false, false, (PageRequest) params.get("page")));
    }

    public static String buildCountByExample(ProviderContext context) {
        return SqlBuilderHelper.buildSql(context, tableInfo -> _buildSelectByExample(SqlBuilderHelper.getEntityClass(context), tableInfo, true, false, null));
    }

    private static String _buildSelectByExample(Class<?> entityClass, EntityTableInfo tableInfo, boolean isCount, boolean hasLimit, @Nullable PageRequest page) {
        StringBuilder sqlBuffer = new StringBuilder();
        sqlBuffer.append("<script>");
        sqlBuffer.append("SELECT ");
        sqlBuffer.append(isCount ? "COUNT(0)" : "*");
        sqlBuffer.append(" FROM ").append(SqlBuilderHelper.buildTableName(tableInfo));
        sqlBuffer.append("<where>");
        for (PropertyDescriptor prop : BeanUtils.getPropertyDescriptors(entityClass)) {
            Method getter = prop.getReadMethod();
            if (getter == null) {
                continue;
            }
            String fieldName = prop.getName();
            String columnName = tableInfo.getColumnByField(fieldName);
            if (columnName == null) {
                continue;
            }
            sqlBuffer.append("<if test=\"example.").append(fieldName).append(" != null\">AND ").append(columnName).append(" = #{example.").append(fieldName).append("}").append("</if>");
        }
        sqlBuffer.append("</where>");
        if (hasLimit) {
            sqlBuffer.append(" LIMIT 1");
        }
        if(page != null) {
            sqlBuffer.append(" LIMIT ").append(page.getOffset()).append(",").append(page.getSize());
        }
        sqlBuffer.append("</script>");
        return sqlBuffer.toString();
    }

    public static String buildSelectOneByMap(ProviderContext context, Map<String, Object> params) {
        return _buildSelectByMap(context, params, false, true);
    }

    public static String buildSelectByMap(ProviderContext context, Map<String, Object> params) {
        return _buildSelectByMap(context, params, false, false);
    }

    public static String buildCountByMap(ProviderContext context, Map<String, Object> params) {
        return _buildSelectByMap(context, params, true, false);
    }

    private static String _buildSelectByMap(ProviderContext context, Map<String, Object> params, boolean isCount, boolean hasLimit) {
        @SuppressWarnings("unchecked") Map<String, Object> map = (Map<String, Object>) params.get("map");
        Class<?> entityClass = SqlBuilderHelper.getEntityClass(context);
        EntityTableInfo tableInfo = SqlBuilderHelper.getTableInfo(entityClass);
        StringBuilder sqlBuffer = new StringBuilder();
        sqlBuffer.append("<script>");
        sqlBuffer.append("SELECT ");
        sqlBuffer.append(isCount ? "COUNT(0)" : "*");
        sqlBuffer.append(" FROM ").append(SqlBuilderHelper.buildTableName(tableInfo));
        sqlBuffer.append("<where>");
        for (String fieldName : map.keySet()) {
            String columnName = tableInfo.getColumnByField(fieldName);
            if (columnName == null) {
                continue;
            }
            sqlBuffer.append("<if test=\"map.").append(fieldName).append(" != null\">AND ").append(columnName).append(" = #{map.").append(fieldName).append("}").append("</if>");
        }
        sqlBuffer.append("</where>");
        if (hasLimit) {
            sqlBuffer.append(" LIMIT 1");
        }
        sqlBuffer.append("</script>");
        return sqlBuffer.toString();
    }
}
