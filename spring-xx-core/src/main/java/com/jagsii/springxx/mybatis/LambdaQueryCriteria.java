package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.SerializableFunction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LambdaQueryCriteria<T> extends AbstractQueryCriteria<T, SerializableFunction<T, ?>, LambdaQueryCriteria<T>> {
    protected LambdaQueryCriteria(Class<T> entityClass) {
        super(entityClass);
    }

    protected LambdaQueryCriteria(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        super(entityClass, params, paramNameSeq);
    }

    @Override
    protected String columnToString(SerializableFunction<T, ?> column) {
        return lambdaMethodToColumn(column, this.entityClass);
    }

    @Override
    protected LambdaQueryCriteria<T> instantiate(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        return new LambdaQueryCriteria<>(entityClass, params, paramNameSeq);
    }
}
