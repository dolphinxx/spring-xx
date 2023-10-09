package com.jagsii.springxx.common.base;

import com.jagsii.springxx.common.pagination.Page;
import com.jagsii.springxx.common.pagination.PageRequest;
import com.jagsii.springxx.mybatis.Criteria;

import java.util.List;
import java.util.Map;

public interface BaseService<T, ID> {
    T findById(ID id);

    List<T> findByExample(T example);

    Page<T> findPageByExample(T example, PageRequest page);

    List<T> findByMap(Map<String, Object> map);

    Page<T> findPageByMap(Map<String, Object> map, PageRequest page);

    List<T> findByCriteria(Criteria criteria);

    Page<T> findPageByCriteria(Criteria criteria, PageRequest page);

    int countByExample(T example);

    int countByMap(Map<String, Object> map);

    int countByCriteria(Criteria criteria);

    void save(T entity);

    void upsert(T entity);

    void updateById(T entity);

    void deleteById(ID id);

    void deleteByIds(List<ID> ids);
}
