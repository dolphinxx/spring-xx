package com.jagsii.springxx.mybatis;

public class Criterion {
    public static <T> QueryCriteria<T> query(Class<T> entityClass) {
        return new QueryCriteria<>(entityClass);
    }

    public static <T> LambdaQueryCriteria<T> lambdaQuery(Class<T> entityClass) {
        return new LambdaQueryCriteria<>(entityClass);
    }

    public static <T> UpdateCriteria<T> update(Class<T> entityClass) {
        return new UpdateCriteria<>(entityClass);
    }

    public static <T> LambdaUpdateCriteria<T> lambdaUpdate(Class<T> entityClass) {
        return new LambdaUpdateCriteria<>(entityClass);
    }
}
