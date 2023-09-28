package com.jagsii.springxx.mybatis;

import org.apache.ibatis.annotations.*;

public interface CrudBaseMapper<T, ID> {
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @InsertProvider(type = CrudSqlBuilder.class, method = "buildInsert")
    int insert(T entity);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @InsertProvider(type = CrudSqlBuilder.class, method = "buildInsertSelective")
    int insertSelective(T entity);

    @UpdateProvider(type = CrudSqlBuilder.class, method = "buildUpdateByPrimaryKey")
    int updateByPrimaryKey(T entity);

    @UpdateProvider(type = CrudSqlBuilder.class, method = "buildUpdateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(T entity);

    @DeleteProvider(type = CrudSqlBuilder.class, method = "buildDeleteByPrimaryKey")
    int deleteByPrimaryKey(ID id);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildSelectByPrimaryKey")
    T selectByPrimaryKey(ID id);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildExistsByPrimaryKey")
    boolean existsByPrimaryKey(ID id);
}
