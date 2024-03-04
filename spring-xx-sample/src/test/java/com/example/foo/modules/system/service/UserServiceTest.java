package com.example.foo.modules.system.service;

import com.example.foo.modules.system.entity.User;
import com.example.foo.modules.system.mapper.UserMapper;
import com.jagsii.springxx.test.ServiceTests;
import org.instancio.Instancio;
import org.instancio.Select;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
        UserMybatisService.class
})
@MapperScan(basePackageClasses = UserMapper.class)
public class UserServiceTest extends ServiceTests {
    @SuppressWarnings("unused")
    @WithSettings
    final Settings settings = Settings.create()
            .set(Keys.BEAN_VALIDATION_ENABLED, true);
    @Autowired
    UserService userService;

    @Test
    void findByExample() {
        List<User> items = Instancio.ofList(User.class).size(10).ignore(Select.field(User::getId)).create();
        for (User item : items) {
            namedParameterJdbcTemplate.update("INSERT INTO `user`(`name`, `username`, `password`, `birth`, `status`, `remark`, `create_time`, `update_time`)VALUES(:name, :username, :password, :birth, :status, :remark, :createTime, :updateTime)", beanToMap(item));
        }
        User example = new User();
        List<User> actual = userService.findByExample(example);
        assertThat(actual).hasSize(10);
    }
}
