package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.test.MybatisMapperTests;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CriteriaBaseMapperTest extends MybatisMapperTests {
    interface UserMapper extends CriteriaBaseMapper<User> {
    }

    @Test
    void countByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "BF");
        testMapper(mapper -> {
            int actual = mapper.countByCriteria(Criterion.query(User.class).startsWith("name", "F"));
            assertThat(actual).isEqualTo(2);
        }, UserMapper.class);
    }

    @Test
    void existsByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "BF");
        testMapper(mapper -> {
            boolean actual = mapper.existsByCriteria(Criterion.query(User.class).startsWith("name", "F"));
            assertThat(actual).isTrue();
            actual = mapper.existsByCriteria(Criterion.query(User.class).startsWith("name", "FF"));
            assertThat(actual).isFalse();
        }, UserMapper.class);
    }

    @Test
    void selectByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "BF");
        testMapper(mapper -> {
            List<User> actual = mapper.selectByCriteria(Criterion.query(User.class).startsWith("name", "F").limit(1, 2));
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).hasFieldOrPropertyWithValue("name", "Foo");
            assertThat(actual.get(1)).hasFieldOrPropertyWithValue("name", "Fish");
        }, UserMapper.class);
    }

    @Test
    void selectByCriteriaWithRowBounds() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "BF");
        testMapper(mapper -> {
            List<User> actual = mapper.selectByCriteriaWithRowBounds(Criterion.query(User.class).startsWith("name", "F"), new RowBounds(1, 2));
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).hasFieldOrPropertyWithValue("name", "Foo");
            assertThat(actual.get(1)).hasFieldOrPropertyWithValue("name", "Fish");
        }, UserMapper.class);
    }

    @Test
    void updateByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        testMapper(mapper -> mapper.updateByCriteria(Criterion.update(User.class).set("name", "Fish").eq("name", "Foo")), UserMapper.class);
        List<User> actual = jdbcTemplate.query("SELECT * FROM `user` WHERE name = ?", new BeanPropertyRowMapper<>(User.class), "Fish");
        assertThat(actual).hasSize(2);
    }

    @Test
    void deleteByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        testMapper(mapper -> mapper.deleteByCriteria(Criterion.query(User.class).eq("name", "Foo")), UserMapper.class);
        Integer actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user` WHERE name = ?", int.class, "Foo");
        assertThat(actual).isEqualTo(0);
    }
}