package com.jagsii.springxx.mybatis;

public enum SqlOperator {
    EQ("="),
    NE("<>"),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    IN("IN"),
    NOT_IN("NOT IN"),
    EXISTS("EXISTS"),
    NOT_EXISTS("NOT EXISTS"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    BETWEEN("BETWEEN"),
    NOT_BETWEEN("NOT BETWEEN"),

    ;
    public final String sql;
    SqlOperator(String sql) {
        this.sql = sql;
    }
}
