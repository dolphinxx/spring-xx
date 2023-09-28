package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.mybatis.enums.OrderDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractQueryCriteria<T, C, I extends AbstractCriteria<T, C, I>> extends AbstractCriteria<T, C, I> {
    protected List<String> orderBys = new ArrayList<>();
    protected String limitSql;

    protected AbstractQueryCriteria(Class<T> entityClass) {
        super(entityClass);
    }

    protected AbstractQueryCriteria(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        super(entityClass, params, paramNameSeq);
    }

    @SuppressWarnings("unchecked")
    public I orderBy(String column, OrderDirection direction) {
        orderBys.add(formatColumn(column) + " " + direction.name());
        return (I) this;
    }

    public I limit(int count) {
        return limit(0, count);
    }

    @SuppressWarnings("unchecked")
    public I limit(int offset, int count) {
        this.limitSql = " LIMIT " + offset + "," + count;
        return (I) this;
    }

    public String buildOrderBySql() {
        if (orderBys.isEmpty()) {
            return "";
        }
        return " ORDER BY " + String.join(", ", orderBys);
    }

    public String buildLimitSql() {
        return limitSql == null ? "" : limitSql;
    }
}
