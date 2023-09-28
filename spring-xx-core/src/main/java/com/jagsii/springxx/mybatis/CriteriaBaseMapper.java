package com.jagsii.springxx.mybatis;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CriteriaBaseMapper<T> {
    @SelectProvider(type = CriteriaSqlBuilder.class, method = "buildCountByCriteria")
    int countByCriteria(@Param("criteria") Criteria<T> criteria);

    @SelectProvider(type = CriteriaSqlBuilder.class, method = "buildExistsByCriteria")
    boolean existsByCriteria(@Param("criteria") Criteria<T> criteria);

    @SelectProvider(type = CriteriaSqlBuilder.class, method = "buildSelectByCriteria")
    List<T> selectByCriteria(@Param("criteria") Criteria<T> criteria);

    @SelectProvider(type = CriteriaSqlBuilder.class, method = "buildSelectByCriteria")
    List<T> selectByCriteriaWithRowBounds(@Param("criteria") Criteria<T> criteria, RowBounds rowBounds);

    @UpdateProvider(type = CriteriaSqlBuilder.class, method = "buildUpdateByCriteria")
    int updateByCriteria(@Param("criteria") Criteria<T> criteria);

    @DeleteProvider(type = CriteriaSqlBuilder.class, method = "buildDeleteByCriteria")
    int deleteByCriteria(@Param("criteria") Criteria<T> criteria);
}
