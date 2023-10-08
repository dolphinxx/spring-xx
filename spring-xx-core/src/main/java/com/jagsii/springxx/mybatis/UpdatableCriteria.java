package com.jagsii.springxx.mybatis;

public interface UpdatableCriteria<COLUMN, I> extends Criteria {
    I set(COLUMN column, Object value);

    String buildSetsSql();
}
