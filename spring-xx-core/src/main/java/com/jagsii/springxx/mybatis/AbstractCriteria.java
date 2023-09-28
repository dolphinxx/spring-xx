package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.SerializableFunction;
import com.jagsii.springxx.common.utils.NameUtils;
import com.jagsii.springxx.common.utils.Reflects;
import com.jagsii.springxx.mybatis.enums.LikeType;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jagsii.springxx.mybatis.SqlConstant.IDENTIFIER_ESCAPE_CHAR;

public abstract class AbstractCriteria<T, C, I extends AbstractCriteria<T, C, I>> implements Criteria<T> {
    protected final Class<T> entityClass;
    @Getter
    protected final Map<String, Object> params;
    protected final AtomicInteger paramNameSeq;
    protected final List<Object> expressions = new ArrayList<>();

    @SuppressWarnings("unchecked")
    protected final I typedThis = (I) this;

    enum Logical {
        AND,
        OR,
        NOT
    }

    static class SubExpression<T, C, I extends AbstractCriteria<T, C, I>> {
        protected I criteria;

        protected SubExpression(I criteria) {
            this.criteria = criteria;
        }
    }

    protected static <S>String lambdaMethodToColumn(SerializableFunction<S, ?> column, Class<S> entityClass) {
        String methodName = Reflects.methodNameFromLambda(column);
        String propName = NameUtils.methodToProperty(methodName);
        Map<String, String> columns = SqlBuilderHelper.getTableInfo(entityClass).getColumns();
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            if (propName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("column not found for method: " + methodName);
    }

    protected AbstractCriteria(Class<T> entityClass) {
        this(entityClass, new HashMap<>(), new AtomicInteger(0));
    }

    protected AbstractCriteria(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq) {
        this.entityClass = entityClass;
        this.params = params;
        this.paramNameSeq = paramNameSeq;
    }

    protected abstract String columnToString(C column);

    protected abstract I instantiate(Class<T> entityClass, Map<String, Object> params, AtomicInteger paramNameSeq);

    protected String wrapParam(Object value) {
        String paramName = "param" + paramNameSeq.incrementAndGet();
        this.params.put(paramName, value);
        return "#{criteria.params." + paramName + "}";
    }

    protected I addCondition(C column, SqlOperator operator, Object value) {
        this.expressions.add(formatColumn(columnToString(column)) + " " + operator.sql + " " + wrapParam(value));
        return typedThis;
    }

    protected I addCondition(C column, SqlOperator operator) {
        this.expressions.add(formatColumn(columnToString(column)) + " " + operator.sql);
        return typedThis;
    }

    protected I addConditionSql(C column, SqlOperator operator, String valueSql) {
        this.expressions.add(formatColumn(columnToString(column)) + " " + operator.sql + " " + valueSql);
        return typedThis;
    }

    public I sub() {
        I e = instantiate(this.entityClass, this.params, this.paramNameSeq);
        this.expressions.add(new SubExpression<>(e));
        return e;
    }

    public I and() {
        this.expressions.add(Logical.AND);
        return typedThis;
    }

    public I or() {
        this.expressions.add(Logical.OR);
        return typedThis;
    }

    public I eq(C column, Object value) {
        return this.addCondition(column, SqlOperator.EQ, value);
    }

    public I eqSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.EQ, valueSql);
    }

    public I ne(C column, Object value) {
        return this.addCondition(column, SqlOperator.NE, value);
    }

    public I neSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.NE, valueSql);
    }

    public I gt(C column, Object value) {
        return this.addCondition(column, SqlOperator.GT, value);
    }

    public I gtSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.GT, valueSql);
    }

    public I gte(C column, Object value) {
        return this.addCondition(column, SqlOperator.GTE, value);
    }

    public I gteSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.GTE, valueSql);
    }

    public I lt(C column, Object value) {
        return this.addCondition(column, SqlOperator.LT, value);
    }

    public I ltSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.LT, valueSql);
    }

    public I lte(C column, Object value) {
        return this.addCondition(column, SqlOperator.LTE, value);
    }

    public I lteSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.LTE, valueSql);
    }

    public I isNull(C column) {
        return this.addCondition(column, SqlOperator.IS_NULL);
    }

    public I isNotNull(C column) {
        return this.addCondition(column, SqlOperator.IS_NOT_NULL);
    }

    public I in(C column, Object... values) {
        if (values.length == 0) {
            // skip empty values
            return typedThis;
        }
        return this.addConditionSql(column, SqlOperator.IN, buildInExpression(Arrays.stream(values)));
    }

    public I in(C column, Collection<?> values) {
        if (values.size() == 0) {
            // skip empty values
            return typedThis;
        }
        return this.addConditionSql(column, SqlOperator.IN, buildInExpression(values.stream()));
    }

    public I inSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.IN, '(' + valueSql + ')');
    }

    public I notIn(C column, Object... values) {
        if (values.length == 0) {
            // skip empty values
            return typedThis;
        }
        return this.addConditionSql(column, SqlOperator.NOT_IN, buildInExpression(Arrays.stream(values)));
    }

    public I notIn(C column, Collection<?> values) {
        if (values.size() == 0) {
            // skip empty values
            return typedThis;
        }
        return this.addConditionSql(column, SqlOperator.NOT_IN, buildInExpression(values.stream()));
    }

    public I notInSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.NOT_IN, '(' + valueSql + ')');
    }

    /**
     * LIKE "%value%"
     */
    public I like(C column, Object value) {
        return this.contains(column, value);
    }

    /**
     * LIKE "%value%"
     */
    public I contains(C column, Object value) {
        return this.addConditionSql(column, SqlOperator.LIKE, buildLikeExpression(value.toString(), LikeType.CONTAINS, false));
    }

    /**
     * LIKE "value%"
     */
    public I startsWith(C column, Object value) {
        return this.addConditionSql(column, SqlOperator.LIKE, buildLikeExpression(value.toString(), LikeType.STARTS_WITH, false));
    }

    /**
     * LIKE "%value"
     */
    public I endsWith(C column, Object value) {
        return this.addConditionSql(column, SqlOperator.LIKE, buildLikeExpression(value.toString(), LikeType.ENDS_WITH, false));
    }

    public I likeSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.LIKE, valueSql);
    }

    /**
     * NOT LIKE "%value%"
     */
    public I notContains(C column, Object value) {
        return this.addConditionSql(column, SqlOperator.NOT_LIKE, buildLikeExpression(value.toString(), LikeType.CONTAINS, false));
    }

    /**
     * NOT LIKE "value%"
     */
    public I notStartsWith(C column, Object value) {
        return this.addConditionSql(column, SqlOperator.NOT_LIKE, buildLikeExpression(value.toString(), LikeType.STARTS_WITH, false));
    }

    /**
     * NOT LIKE "%value"
     */
    public I notEndsWith(C column, Object value) {
        return this.addConditionSql(column, SqlOperator.NOT_LIKE, buildLikeExpression(value.toString(), LikeType.ENDS_WITH, false));
    }

    public I notLikeSql(C column, String valueSql) {
        return this.addConditionSql(column, SqlOperator.NOT_LIKE, valueSql);
    }

    /**
     * BETWEEN start AND end
     */
    public I between(C column, Object start, Object end) {
        return this.addConditionSql(column, SqlOperator.BETWEEN, wrapParam(start) + " AND " + wrapParam(end));
    }

    public I betweenSql(C column, String startSql, String endSql) {
        return this.addConditionSql(column, SqlOperator.BETWEEN, startSql + " AND " + endSql);
    }

    /**
     * NOT BETWEEN start AND end
     */
    public I notBetween(C column, Object start, Object end) {
        return this.addConditionSql(column, SqlOperator.NOT_BETWEEN, wrapParam(start) + " AND " + wrapParam(end));
    }

    public I notBetweenSql(C column, String startSql, String endSql) {
        return this.addConditionSql(column, SqlOperator.NOT_BETWEEN, startSql + " AND " + endSql);
    }

    protected String buildLikeExpression(String value, LikeType likeType, boolean single) {
        char wildcard = single ? SqlConstant.LIKE_SINGLE_WILDCARD_CHAR : SqlConstant.LIKE_WILDCARD_CHAR;
        String escapedValue = value.replace("%", SqlConstant.STRING_ESCAPE_CHAR + "%").replace("_", SqlConstant.STRING_ESCAPE_CHAR + "_");
        if (likeType == LikeType.ENDS_WITH) {
            return wrapParam(wildcard + escapedValue);
        }
        if (likeType == LikeType.STARTS_WITH) {
            return wrapParam(escapedValue + wildcard);
        }
        return wrapParam(wildcard + escapedValue + wildcard);
    }

    protected String formatColumn(String column) {
        return IDENTIFIER_ESCAPE_CHAR + column + IDENTIFIER_ESCAPE_CHAR;
    }

    protected String wrapValueSql(String valueSql) {
        return SqlConstant.STRING_QUOTE + valueSql.replace("\"", "\\\"") + SqlConstant.STRING_QUOTE;
    }

    protected String buildInExpression(Stream<?> values) {
        return "(" + values.map(this::wrapParam).collect(Collectors.joining(", ")) + ")";
    }

    private String sqlCache;

    @Override
    public String buildSql() {
        if (sqlCache == null) {
            final StringBuilder buff = new StringBuilder();
            boolean isPreviousAndOr = false;
            for (int i = 0, len = this.expressions.size(); i < len; i++) {
                Object c = this.expressions.get(i);
                if (c == Logical.AND) {
                    isPreviousAndOr = true;
                    buff.append(" AND ");
                    continue;
                }
                if (c == Logical.OR) {
                    isPreviousAndOr = true;
                    buff.append(" OR ");
                    continue;
                }
                if (i > 0 && !isPreviousAndOr) {
                    buff.append(" AND ");
                }
                isPreviousAndOr = false;
                Class<?> clazz = c.getClass();
                if (clazz.equals(SubExpression.class)) {
                    @SuppressWarnings("unchecked") I inner = ((SubExpression<T, C, I>) c).criteria;
                    if (inner.expressions.isEmpty()) {
                        // skip empty expressions
                        continue;
                    }
                    if (inner.expressions.size() == 1) {
                        buff.append(inner.buildSql());
                    } else {
                        buff.append("(").append(inner.buildSql()).append(")");
                    }
                    continue;
                }
                buff.append(c);
            }
            sqlCache = buff.toString();
        }
        return sqlCache;
    }
}
