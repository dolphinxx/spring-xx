package com.jagsii.springxx.common.base;

import com.jagsii.springxx.common.pagination.Page;
import com.jagsii.springxx.common.pagination.PageRequest;
import com.jagsii.springxx.mybatis.AbstractQueryCriteria;
import com.jagsii.springxx.mybatis.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseMybatisService<T, ID, MAPPER extends BaseMapper<T, ID>> implements BaseService<T, ID> {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected MAPPER mapper;

    protected MAPPER getMapper() {
        return mapper;
    }

    @Override
    public T findById(ID id) {
        return getMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<T> findByExample(T example) {
        return getMapper().selectByExample(example);
    }

    @Override
    public Page<T> findPageByExample(T example, PageRequest page) {
        int total = getMapper().countByExample(example);
        List<T> items = total == 0 ? Collections.emptyList() : getMapper().selectByExampleWithPage(example, page);
        Page<T> paginate = new Page<>(page, total);
        paginate.setItems(items);
        return paginate;
    }

    @Override
    public List<T> findByMap(Map<String, Object> map) {
        return getMapper().selectByMap(map);
    }

    @Override
    public Page<T> findPageByMap(Map<String, Object> map, PageRequest page) {
        int total = getMapper().countByMap(map);
        map.put("offset", page.getOffset());
        map.put("limit", page.getSize());
        List<T> items = total == 0 ? Collections.emptyList() : getMapper().selectByMap(map);
        return new Page<>(page, total, items);
    }

    @Override
    public List<T> findByCriteria(Criteria criteria) {
        return getMapper().selectByCriteria(criteria);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Page<T> findPageByCriteria(Criteria criteria, PageRequest page) {
        int total = getMapper().countByCriteria(criteria);
        ((AbstractQueryCriteria) criteria).limit(page.getOffset(), page.getSize());
        List<T> items = total == 0 ? Collections.emptyList() : getMapper().selectByCriteria(criteria);
        Page<T> pagination = new Page<>(page, total);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public int countByExample(T example) {
        return getMapper().countByExample(example);
    }

    @Override
    public int countByMap(Map<String, Object> map) {
        return getMapper().countByMap(map);
    }

    @Override
    public int countByCriteria(Criteria criteria) {
        return getMapper().countByCriteria(criteria);
    }

    @Override
    @Transactional
    public void save(T entity) {
        getMapper().insertSelective(entity);
    }

    @Override
    @Transactional
    public void upsert(T entity) {
        getMapper().upsert(entity);
    }

    @Override
    @Transactional
    public void updateById(T entity) {
        getMapper().updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        getMapper().deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void deleteByIds(List<ID> ids) {
        getMapper().deleteByPrimaryKeys(ids);
    }
}
