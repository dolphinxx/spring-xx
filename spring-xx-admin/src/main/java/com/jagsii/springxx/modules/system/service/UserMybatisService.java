package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseMybatisService;
import com.jagsii.springxx.modules.system.entity.User;
import com.jagsii.springxx.modules.system.mapper.UserMapper;
import com.jagsii.springxx.mybatis.Criterion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMybatisService extends BaseMybatisService<User, Long, UserMapper> implements UserService {
    @Override
    public void updatePassword(Long id, String newPassword) {
        getMapper().updateByCriteria(Criterion.lambdaUpdate(User.class).set(User::getPassword, newPassword).eq(User::getId, id));
    }

    @Override
    public User findByUsername(String username) {
        List<User> result = getMapper().selectByCriteria(Criterion.lambdaQuery(User.class).eq(User::getUsername, username).limit(1));
        return result.isEmpty() ? null : result.get(0);
    }
}
