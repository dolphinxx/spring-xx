package com.jagsii.springxx.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateCriteria<T> extends AbstractCriteria<T, String, UpdateCriteria<T>> implements UpdatableCriteria<String, UpdateCriteria<T>> {
    List<String> sets = new ArrayList<>();

    protected UpdateCriteria(Class<T> entityClass) {
        super(entityClass, new HashMap<>(), new AtomicInteger(0));
    }

    protected UpdateCriteria(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        super(entityClass, params, paramNameSeq);
    }

    @Override
    protected UpdateCriteria<T> instantiate(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        return new UpdateCriteria<>(entityClass, params, paramNameSeq);
    }

    @Override
    protected String columnToString(String column) {
        return column;
    }

    @Override
    public UpdateCriteria<T> set(String column, Object value) {
        sets.add(formatColumn(columnToString(column)) + " = " + wrapParam(value));
        return this;
    }

    @Override
    public String buildSetsSql() {
        return String.join(", ", sets);
    }
}
