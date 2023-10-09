package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.common.base.BaseService;
import com.jagsii.springxx.modules.system.entity.User;

public interface UserService extends BaseService<User, Long> {
    /**
     * @param id          user id
     * @param newPassword new password (encoded)
     */
    void updatePassword(Long id, String newPassword);

    User findByUsername(String username);
}
