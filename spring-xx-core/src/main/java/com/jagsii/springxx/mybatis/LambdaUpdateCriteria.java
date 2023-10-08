package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.SerializableFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LambdaUpdateCriteria<T> extends AbstractCriteria<T, SerializableFunction<T, ?>, LambdaUpdateCriteria<T>> implements UpdatableCriteria<SerializableFunction<T, ?>, LambdaUpdateCriteria<T>> {
    List<String> sets = new ArrayList<>();

    protected LambdaUpdateCriteria(Class<T> entityClass) {
        super(entityClass);
    }

    protected LambdaUpdateCriteria(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameEq) {
        super(entityClass, params, paramNameEq);
    }

    @Override
    protected String columnToString(SerializableFunction<T, ?> column) {
        return lambdaMethodToColumn(column, this.entityClass);
    }

    @Override
    protected LambdaUpdateCriteria<T> instantiate(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        return new LambdaUpdateCriteria<>(entityClass, params, paramNameSeq);
    }

    @Override
    public LambdaUpdateCriteria<T> set(SerializableFunction<T, ?> column, Object value) {
        sets.add(formatColumn(columnToString(column)) + " = " + wrapParam(value));
        return this;
    }

    @Override
    public String buildSetsSql() {
        return String.join(", ", sets);
    }
}
