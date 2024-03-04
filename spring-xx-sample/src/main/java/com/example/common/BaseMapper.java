package com.example.common;

import com.jagsii.springxx.mybatis.CriteriaBaseMapper;
import com.jagsii.springxx.mybatis.CrudBaseMapper;

public interface BaseMapper<T, ID> extends CrudBaseMapper<T, ID>, CriteriaBaseMapper<T> {
}
