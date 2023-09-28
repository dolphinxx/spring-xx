package com.jagsii.springxx.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    void insertBatch() {
        List<User> entities = new ArrayList<>();
        User entity = new User();
        entity.setName("Foo1");
        entity.setAddress("abc1");
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entities.add(entity);
        entity = new User();
        entity.setName("Foo2");
        entity.setAddress("abc2");
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entities.add(entity);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).insertBatch(entities);
        }
        assertThat(entity.getId()).isNotNull();
        List<User> actual = jdbcTemplate.query("SELECT * FROM `user` ORDER BY `name`", new BeanPropertyRowMapper<>(User.class));
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).hasFieldOrPropertyWithValue("name", "Foo1")
                .hasFieldOrPropertyWithValue("address", "abc1")
                .hasFieldOrPropertyWithValue("birth", null)
                .hasFieldOrPropertyWithValue("createTime", now)
                .hasFieldOrPropertyWithValue("updateTime", now);
        assertThat(actual.get(1)).hasFieldOrPropertyWithValue("name", "Foo2")
                .hasFieldOrPropertyWithValue("address", "abc2")
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
        long id = insertForGeneratedId("INSERT INTO `user`(`name`, `birth`)VALUES(?, ?)", "Foo", LocalDate.parse("2000-01-01"));
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
    void upsert() {
        User entity = new User();
        entity.setName("Foo");
        entity.setAddress("abc");
        entity.setBirth(LocalDate.parse("2000-01-01"));
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.upsert(entity);
            Long id = entity.getId();
            assertThat(id).isNotNull();
            User actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), id);
            assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo")
                    .hasFieldOrPropertyWithValue("address", "abc")
                    .hasFieldOrPropertyWithValue("birth", LocalDate.parse("2000-01-01"))
                    .hasFieldOrPropertyWithValue("createTime", now)
                    .hasFieldOrPropertyWithValue("updateTime", now);

            entity.setName("Fish");
            entity.setBirth(null);
            mapper.upsert(entity);
            actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), id);
            assertThat(actual).hasFieldOrPropertyWithValue("name", "Fish")
                    .hasFieldOrPropertyWithValue("address", "abc")
                    .hasFieldOrPropertyWithValue("birth", null)
                    .hasFieldOrPropertyWithValue("createTime", now)
                    .hasFieldOrPropertyWithValue("updateTime", now);
        }
    }

    @Test
    void upsertSelective() {
        User entity = new User();
        entity.setName("Foo");
        entity.setBirth(LocalDate.parse("2000-01-01"));
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.upsertSelective(entity);
            assertThat(entity.getId()).isNotNull();
            User actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), entity.getId());
            assertThat(actual).isNotNull();
            assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo")
                    .hasFieldOrPropertyWithValue("birth", LocalDate.parse("2000-01-01"))
                    .hasFieldOrPropertyWithValue("address", "");
            assertThat(actual.getCreateTime()).isNotNull();
            assertThat(actual.getUpdateTime()).isNotNull();

            entity.setName("Fish");
            entity.setBirth(null);
            mapper.upsertSelective(entity);
            actual = jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE id = ?", new BeanPropertyRowMapper<>(User.class), entity.getId());
            assertThat(actual).isNotNull();
            assertThat(actual).hasFieldOrPropertyWithValue("name", "Fish")
                    .hasFieldOrPropertyWithValue("birth", LocalDate.parse("2000-01-01"))
                    .hasFieldOrPropertyWithValue("address", "");
            assertThat(actual.getCreateTime()).isNotNull();
            assertThat(actual.getUpdateTime()).isNotNull();
        }
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
    void deleteByPrimaryKeys() {
        Long id1 = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        Long id2 = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Fish");
        Integer actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user`", Integer.class);
        assertThat(actual).isEqualTo(2);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.getMapper(UserMapper.class).deleteByPrimaryKeys(Lists.list(id1, id2));
        }
        actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user`", Integer.class);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void selectByPrimaryKey() {
        long id = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            User actual = sqlSession.getMapper(UserMapper.class).selectByPrimaryKey(id);
            assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo");
        }
    }

    @Test
    void existsByPrimaryKey() {
        long id = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            boolean actual = sqlSession.getMapper(UserMapper.class).existsByPrimaryKey(id);
            assertThat(actual).isTrue();
            actual = sqlSession.getMapper(UserMapper.class).existsByPrimaryKey(0L);
            assertThat(actual).isFalse();
        }
    }
}