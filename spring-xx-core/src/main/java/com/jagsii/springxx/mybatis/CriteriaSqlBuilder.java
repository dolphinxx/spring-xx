package com.jagsii.springxx.mybatis;

import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Map;

public class CriteriaSqlBuilder {
    public static String buildCountByCriteria(ProviderContext context, Map<String, Object> params) {
        EntityTableInfo tableInfo = SqlBuilderHelper.getTableInfo(context);
        QueryCriteria<?> criteria = (QueryCriteria<?>) params.get("criteria");
        return "SELECT COUNT(0) FROM " +
                SqlBuilderHelper.buildTableName(tableInfo) +
                " WHERE " +
                criteria.buildSql();
    }

    public static String buildSelectByCriteria(ProviderContext context, Map<String, Object> params) {
        EntityTableInfo tableInfo = SqlBuilderHelper.getTableInfo(context);
        QueryCriteria<?> criteria = (QueryCriteria<?>) params.get("criteria");
        return "SELECT * FROM " +
                SqlBuilderHelper.buildTableName(tableInfo) +
                " WHERE " +
                criteria.buildSql() + criteria.buildOrderBySql() + criteria.buildLimitSql();
    }

    public static String buildUpdateByCriteria(ProviderContext context, Map<String, Object> params) {
        EntityTableInfo tableInfo = SqlBuilderHelper.getTableInfo(context);
        UpdateCriteria<?> criteria = (UpdateCriteria<?>) params.get("criteria");
        return "UPDATE " + SqlBuilderHelper.buildTableName(tableInfo) + " SET " + criteria.buildSetsSql() + " WHERE " + criteria.buildSql();
    }

    public static String buildDeleteByCriteria(ProviderContext context, Map<String, Object> params) {
        EntityTableInfo tableInfo = SqlBuilderHelper.getTableInfo(context);
        QueryCriteria<?> criteria = (QueryCriteria<?>) params.get("criteria");
        return "DELETE FROM " + SqlBuilderHelper.buildTableName(tableInfo) + " WHERE " + criteria.buildSql();
    }
}
