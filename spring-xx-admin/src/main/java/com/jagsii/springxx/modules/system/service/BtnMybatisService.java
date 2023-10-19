package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.modules.system.entity.Btn;
import com.jagsii.springxx.modules.system.mapper.BtnMapper;
import org.springframework.stereotype.Service;

@Service
public class BtnMybatisService extends BaseMybatisService<Btn, Long, BtnMapper> implements BtnService {
}
