package com.jagsii.springxx.mybatis;

public interface Update<COLUMN, I> {
    I set(COLUMN column, Object value);

    String buildSetsSql();
}
