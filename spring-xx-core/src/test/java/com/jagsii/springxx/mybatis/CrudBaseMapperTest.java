package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.pagination.PageRequest;
import com.jagsii.springxx.test.MybatisMapperTests;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CrudBaseMapperTest extends MybatisMapperTests {
    interface UserMapper extends CrudBaseMapper<User, Long> {
    }

    @Test
    void insert() {
        User entity = new User();
        entity.setName("Foo");
        entity.setAddress("abc");
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        testMapper(mapper -> mapper.insert(entity), UserMapper.class);
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
        testMapper(mapper -> mapper.insertBatch(entities), UserMapper.class);
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
        testMapper(mapper -> mapper.insertSelective(entity), UserMapper.class);
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
        testMapper(mapper -> mapper.updateByPrimaryKey(entity), UserMapper.class);
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
        testMapper(mapper -> mapper.updateByPrimaryKeySelective(entity), UserMapper.class);
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
        testMapper(mapper -> {
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
        }, UserMapper.class);
    }

    @Test
    void upsertSelective() {
        User entity = new User();
        entity.setName("Foo");
        entity.setBirth(LocalDate.parse("2000-01-01"));
        testMapper(mapper -> {
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
        }, UserMapper.class);
    }

    @Test
    void deleteByPrimaryKey() {
        Long id = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        Integer actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user` WHERE id = ?", Integer.class, id);
        assertThat(actual).isEqualTo(1);
        testMapper(mapper -> mapper.deleteByPrimaryKey(id), UserMapper.class);
        actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user` WHERE id = ?", Integer.class, id);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void deleteByPrimaryKeys() {
        Long id1 = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        Long id2 = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Fish");
        Integer actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user`", Integer.class);
        assertThat(actual).isEqualTo(2);
        testMapper(mapper -> mapper.deleteByPrimaryKeys(Lists.list(id1, id2)), UserMapper.class);
        actual = jdbcTemplate.queryForObject("SELECT COUNT(0) FROM `user`", Integer.class);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void selectByPrimaryKey() {
        long id = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        testMapper(mapper -> {
            User actual = mapper.selectByPrimaryKey(id);
            assertThat(actual).hasFieldOrPropertyWithValue("name", "Foo");
        }, UserMapper.class);
    }

    @Test
    void existsByPrimaryKey() {
        long id = insertForGeneratedId("INSERT INTO `user`(`name`)VALUES(?)", "Foo");
        testMapper(mapper -> {
            boolean actual = mapper.existsByPrimaryKey(id);
            assertThat(actual).isTrue();
            actual = mapper.existsByPrimaryKey(0L);
            assertThat(actual).isFalse();
        }, UserMapper.class);
    }

    @Test
    void selectOneByExample() {
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC1");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC2");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC3");
        testMapper(mapper -> {
            User example = new User();
            example.setName("Foo");
            User actual = mapper.selectOneByExample(example);
            assertThat(actual).hasFieldOrPropertyWithValue("address", "DC1");
        }, UserMapper.class);
    }

    @Test
    void selectByExample() {
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC1");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC2");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC3");
        testMapper(mapper -> {
            User example = new User();
            example.setName("Foo");
            List<User> actual = mapper.selectByExample(example);
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).hasFieldOrPropertyWithValue("address", "DC1");
            assertThat(actual.get(1)).hasFieldOrPropertyWithValue("address", "DC3");
        }, UserMapper.class);
    }

    @Test
    void selectByExampleWithPage() {
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC1");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC2");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC3");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC4");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC5");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC6");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC7");
        testMapper(mapper -> {
            User example = new User();
            example.setName("Foo");
            List<User> actual = mapper.selectByExampleWithPage(example, new PageRequest(2, 2));
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).hasFieldOrPropertyWithValue("address", "DC5");
            assertThat(actual.get(1)).hasFieldOrPropertyWithValue("address", "DC6");
        }, UserMapper.class);
    }

    @Test
    void countByExample() {
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC1");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC2");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC3");
        testMapper(mapper -> {
            User example = new User();
            example.setName("Foo");
            int actual = mapper.countByExample(example);
            assertThat(actual).isEqualTo(2);
        }, UserMapper.class);
    }

    @Test
    void selectOneByMap() {
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC1");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC2");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC3");
        testMapper(mapper -> {
            User actual = mapper.selectOneByMap(Collections.singletonMap("name", "Foo"));
            assertThat(actual).hasFieldOrPropertyWithValue("address", "DC1");
        }, UserMapper.class);
    }

    @Test
    void selectByMap() {
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC1");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC2");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC3");
        testMapper(mapper -> {
            List<User> actual = mapper.selectByMap(Collections.singletonMap("name", "Foo"));
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0)).hasFieldOrPropertyWithValue("address", "DC1");
            assertThat(actual.get(1)).hasFieldOrPropertyWithValue("address", "DC3");
        }, UserMapper.class);
    }

    @Test
    void countByMap() {
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC1");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Fish", "DC2");
        jdbcTemplate.update("INSERT INTO `user`(`name`, `address`)VALUES(?, ?)", "Foo", "DC3");
        testMapper(mapper -> {
            int actual = mapper.countByMap(Collections.singletonMap("name", "Foo"));
            assertThat(actual).isEqualTo(2);
        }, UserMapper.class);
    }
}