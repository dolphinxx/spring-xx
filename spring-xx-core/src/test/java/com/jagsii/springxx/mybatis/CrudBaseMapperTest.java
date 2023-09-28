package com.jagsii.springxx.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class CrudBaseMapperTest extends MapperTests {
    interface UserMapper extends CrudBaseMapper<User, Long> {
    }

    @Override
    protected Class<?> getMapperClass() {
        return UserMapper.class;
    }

    @Test
    void insert() {
        User entity = new User();
        entity.setName("Foo");
        entity.setAddress("abc");
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).insert(entity);
        }
        assertThat(entity.getId()).isNotNull();
        User actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), entity.getId());
        assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo")
                .hasFieldOrPropertyWithValue("address", "abc")
                .hasFieldOrPropertyWithValue("birth", null)
                .hasFieldOrPropertyWithValue("createTime", now)
                .hasFieldOrPropertyWithValue("updateTime", now);
    }

    @Test
    void insertSelective() {
        User entity = new User();
        entity.setName("Foo");
        entity.setBirth(LocalDate.parse("2000-01-01"));
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).insertSelective(entity);
        }
        assertThat(entity.getId()).isNotNull();
        User actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), entity.getId());
        assertThat(actual).isNotNull();
        assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo")
                .hasFieldOrPropertyWithValue("birth", LocalDate.parse("2000-01-01"))
                .hasFieldOrPropertyWithValue("address", "");
        assertThat(actual.getCreateTime()).isNotNull();
        assertThat(actual.getUpdateTime()).isNotNull();
    }

    @Test
    void updateByPrimaryKey() {
        Long id = insertForGeneratedId("INSERT INTO `user`(`name`, `birth`)VALUES(?, ?)", "Foo", "2000-01-01");
        User entity = new User();
        entity.setId(id);
        entity.setName("Fish");
        entity.setAddress("abc");
        entity.setBirth(LocalDate.parse("2000-01-02"));
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).updateByPrimaryKey(entity);
        }
        User actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), entity.getId());
        assertThat(actual).hasFieldOrPropertyWithValue("name", "Fish")
                .hasFieldOrPropertyWithValue("address", "abc")
                .hasFieldOrPropertyWithValue("birth", LocalDate.parse("2000-01-02"))
                .hasFieldOrPropertyWithValue("createTime", now)
                .hasFieldOrPropertyWithValue("updateTime", now);
    }

    @Test
    void updateByPrimaryKeySelective() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `user`(`name`, `birth`)VALUES(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "Foo");
            stmt.setObject(2, LocalDate.parse("2000-01-01"));
            return stmt;
        }, keyHolder);
        long id = ((Number) Objects.requireNonNull(keyHolder.getKeys()).get("id")).longValue();
        User entity = new User();
        entity.setId(id);
        entity.setAddress("abc");
        entity.setBirth(LocalDate.parse("2000-01-02"));
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).updateByPrimaryKeySelective(entity);
        }
        User actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), entity.getId());
        assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo")
                .hasFieldOrPropertyWithValue("address", "abc")
                .hasFieldOrPropertyWithValue("birth", LocalDate.parse("2000-01-02"))
                .hasFieldOrPropertyWithValue("createTime", now);
    }

    @Test
    void deleteByPrimaryKey() {
        Long id = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        Integer actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user` WHERE id = ?", Integer.class, id);
        assertThat(actual).isEqualTo(1);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).deleteByPrimaryKey(id);
        }
        actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user` WHERE id = ?", Integer.class, id);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void selectByPrimaryKey() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `user`(`name`)VALUES(?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "Foo");
            return stmt;
        }, keyHolder);
        long id = ((Number) Objects.requireNonNull(keyHolder.getKeys()).get("id")).longValue();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            User actual = sqlSession.getMapper(UserMapper.class).selectByPrimaryKey(id);
            assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo");
        }
    }
}