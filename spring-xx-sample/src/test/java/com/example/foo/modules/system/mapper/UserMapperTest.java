package com.example.foo.modules.system.mapper;

import com.example.foo.modules.system.entity.User;
import com.jagsii.springxx.test.MybatisMapperTests;
import org.instancio.Instancio;
import org.instancio.Select;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest extends MybatisMapperTests {
    @SuppressWarnings("unused")
    @WithSettings
    final Settings settings = Settings.create()
            .set(Keys.BEAN_VALIDATION_ENABLED, true);

    @Test
    void insertSelective() {
        testMapper(mapper -> {
            User entity = Instancio.of(User.class).ignore(Select.field(User::getId)).create();
            assertThat(entity.getId()).isNull();
            int actual = mapper.insertSelective(entity);
            assertThat(actual).isEqualTo(1);
            assertThat(entity.getId()).isNotNull();
        }, UserMapper.class);
    }
}
