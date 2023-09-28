package com.jagsii.springxx.mybatis;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CriteriaBaseMapperTest extends MapperTests {
    interface UserMapper extends CriteriaBaseMapper<User> {
    }

    @Override
    protected Class<?> getMapperClass() {
        return UserMapper.class;
    }

    @Test
    void countByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "BF");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            int actual = sqlSession.getMapper(UserMapper.class).countByCriteria(Criterion.query(User.class).startsWith("name", "F"));
            assertThat(actual).isEqualTo(2);
        }
    }

    @Test
    void selectByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "BF");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> actual = sqlSession.getMapper(UserMapper.class).selectByCriteria(Criterion.query(User.class).startsWith("name", "F").limit(1, 2));
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).hasFieldOrPropertyWithValue("name", "Foo");
            assertThat(actual.get(1)).hasFieldOrPropertyWithValue("name", "Fish");
        }
    }

    @Test
    void selectByCriteriaWithRowBounds() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "BF");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> actual = sqlSession.getMapper(UserMapper.class).selectByCriteriaWithRowBounds(Criterion.query(User.class).startsWith("name", "F"), new RowBounds(1, 2));
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).hasFieldOrPropertyWithValue("name", "Foo");
            assertThat(actual.get(1)).hasFieldOrPropertyWithValue("name", "Fish");
        }
    }

    @Test
    void updateByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).updateByCriteria(Criterion.update(User.class).set("name", "Fish").eq("name", "Foo"));
        }
        List<User> actual = jdbcTemplate.query("SELECT * FROM `user` WHERE name = ?", new BeanPropertyRowMapper<>(User.class), "Fish");
        assertThat(actual).hasSize(2);
    }

    @Test
    void deleteByCriteria() {
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Far");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Foo");
        jdbcTemplate.update("INSERT INTO `user`(name)VALUES(?)", "Fish");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).deleteByCriteria(Criterion.query(User.class).eq("name", "Foo"));
        }
        Integer actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user` WHERE name = ?", int.class, "Foo");
        assertThat(actual).isEqualTo(0);
    }
}