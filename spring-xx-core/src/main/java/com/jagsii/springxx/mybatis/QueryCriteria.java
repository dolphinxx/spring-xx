package com.jagsii.springxx.mybatis;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryCriteria<T> extends AbstractQueryCriteria<T, String, QueryCriteria<T>> {
    protected QueryCriteria(Class<T> entityClass) {
        super(entityClass);
    }

    protected QueryCriteria(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        super(entityClass, params, paramNameSeq);
    }

    @Override
    protected QueryCriteria<T> instantiate(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        return new QueryCriteria<>(entityClass, params, paramNameSeq);
    }

    @Override
    protected String columnToString(String column) {
        return column;
    }
}
