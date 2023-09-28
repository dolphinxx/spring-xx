package com.jagsii.springxx.mybatis;

import org.apache.ibatis.builder.annotation.ProviderContext;

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
}
