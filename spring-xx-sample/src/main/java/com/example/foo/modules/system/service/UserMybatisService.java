package com.example.foo.modules.system.service;

import com.example.common.BaseMybatisService;
import com.example.foo.modules.system.entity.User;
import com.example.foo.modules.system.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserMybatisService extends BaseMybatisService<User, Long, UserMapper> implements UserService {

}
