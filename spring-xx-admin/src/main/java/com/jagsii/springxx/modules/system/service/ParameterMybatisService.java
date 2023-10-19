package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.modules.system.entity.Parameter;
import com.jagsii.springxx.modules.system.mapper.ParameterMapper;
import org.springframework.stereotype.Service;

@Service
public class ParameterMybatisService extends BaseMybatisService<Parameter, Long, ParameterMapper> implements ParameterService {

}
