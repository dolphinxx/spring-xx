package com.example.foo.modules.system.mapper;

import com.example.common.BaseMapper;
import com.example.foo.modules.system.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User, Long> {
}
