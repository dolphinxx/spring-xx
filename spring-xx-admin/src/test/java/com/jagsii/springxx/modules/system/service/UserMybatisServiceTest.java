package com.jagsii.springxx.modules.system.service;

import com.jagsii.springxx.DbTests;
import com.jagsii.springxx.modules.system.entity.User;
import com.jagsii.springxx.modules.system.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
        UserMybatisService.class
})
@MapperScan(basePackageClasses = UserMapper.class)
class UserMybatisServiceTest extends DbTests {
    @Autowired
    UserMybatisService service;

    @Test
    void updatePassword() {
        jdbcTemplate.update("INSERT INTO`user`(id, name, username, password, creator_id)VALUES(?, ?, ?, ?, ?)", 1L, "Foo", "foo", "{noop}111111", 1L);
        service.updatePassword(1L, "{noop}123456");
        User actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), 1L);
        assertThat(actual).hasFieldOrPropertyWithValue("password", "{noop}123456");
    }

    @Test
    void findByUsername() {
        jdbcTemplate.update("INSERT INTO `user`(name, username, password, creator_id) VALUES(?, ?, ?, ?)", "Boo", "boo", "456", 1L);
        jdbcTemplate.update("INSERT INTO `user`(name, username, password, creator_id) VALUES(?, ?, ?, ?)", "Foo", "foo", "123", 2L);
        User actual = service.findByUsername("foo");
        assertThat(actual).hasFieldOrPropertyWithValue("id", 2L).hasFieldOrPropertyWithValue("name", "Foo");
    }
}