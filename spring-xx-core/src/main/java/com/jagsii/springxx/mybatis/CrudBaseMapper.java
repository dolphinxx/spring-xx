package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.pagination.PageRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface CrudBaseMapper<T, ID> {
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @InsertProvider(type = CrudSqlBuilder.class, method = "buildInsert")
    int insert(T entity);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @InsertProvider(type = CrudSqlBuilder.class, method = "buildInsertBatch")
    int insertBatch(List<T> entities);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @InsertProvider(type = CrudSqlBuilder.class, method = "buildInsertSelective")
    int insertSelective(T entity);

    @UpdateProvider(type = CrudSqlBuilder.class, method = "buildUpdateByPrimaryKey")
    int updateByPrimaryKey(T entity);

    @UpdateProvider(type = CrudSqlBuilder.class, method = "buildUpdateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(T entity);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @InsertProvider(type = CrudSqlBuilder.class, method = "buildUpsert")
    int upsert(T entity);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @InsertProvider(type = CrudSqlBuilder.class, method = "buildUpsertSelective")
    int upsertSelective(T entity);

    @DeleteProvider(type = CrudSqlBuilder.class, method = "buildDeleteByPrimaryKey")
    int deleteByPrimaryKey(ID id);

    @DeleteProvider(type = CrudSqlBuilder.class, method = "buildDeleteByPrimaryKeys")
    int deleteByPrimaryKeys(List<ID> ids);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildSelectByPrimaryKey")
    T selectByPrimaryKey(ID id);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildExistsByPrimaryKey")
    boolean existsByPrimaryKey(ID id);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildSelectOneByExample")
    T selectOneByExample(@Param("example") T example);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildSelectOneByMap")
    T selectOneByMap(@Param("map") Map<String, Object> map);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildSelectByExample")
    List<T> selectByExample(@Param("example") T example);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildSelectByExampleWithPage")
    List<T> selectByExampleWithPage(@Param("example") T example, @Param("page") PageRequest page);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildSelectByMap")
    List<T> selectByMap(@Param("map") Map<String, Object> map);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildCountByExample")
    int countByExample(@Param("example") T example);

    @SelectProvider(type = CrudSqlBuilder.class, method = "buildCountByMap")
    int countByMap(@Param("map") Map<String, Object> map);
}
