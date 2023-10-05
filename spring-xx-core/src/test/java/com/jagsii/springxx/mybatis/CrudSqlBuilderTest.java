package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.common.pagination.PageRequest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CrudSqlBuilderTest extends SqlBuilderTests {
    interface UserMapper extends CrudBaseMapper<User, Long> {

    }

    @Test
    void buildInsert() throws Exception {
        String actual = CrudSqlBuilder.buildInsert(getContext(UserMapper.class.getMethod("insert", Object.class))).trim();
        String expected = "<script>INSERT INTO `user`<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">`id`, `name`, `address`, `birth`, `create_time`, `update_time`, </trim><trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \">#{id}, #{name}, #{address}, #{birth}, #{createTime}, #{updateTime}, </trim></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildInsertBatch() throws Exception {
        String actual = CrudSqlBuilder.buildInsertBatch(getContext(UserMapper.class.getMethod("insertBatch", List.class))).trim();
        String expected = "<script>INSERT INTO `user`<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">`id`, `name`, `address`, `birth`, `create_time`, `update_time`, </trim>VALUES <foreach collection=\"list\" item=\"e\" open=\"(\" separator=\"), (\"  close=\")\"><trim suffixOverrides=\", \">#{e.id}, #{e.name}, #{e.address}, #{e.birth}, #{e.createTime}, #{e.updateTime}, </trim></foreach></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildInsertSelective() throws Exception {
        String actual = CrudSqlBuilder.buildInsertSelective(getContext(UserMapper.class.getMethod("insertSelective", Object.class))).trim();
        String expected = "<script>INSERT INTO `user`<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \"><if test=\"id != null\">`id`, </if><if test=\"name != null\">`name`, </if><if test=\"address != null\">`address`, </if><if test=\"birth != null\">`birth`, </if><if test=\"createTime != null\">`create_time`, </if><if test=\"updateTime != null\">`update_time`, </if></trim><trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \"><if test=\"id != null\">#{id}, </if><if test=\"name != null\">#{name}, </if><if test=\"address != null\">#{address}, </if><if test=\"birth != null\">#{birth}, </if><if test=\"createTime != null\">#{createTime}, </if><if test=\"updateTime != null\">#{updateTime}, </if></trim></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildUpdateByPrimaryKey() throws Exception {
        String actual = CrudSqlBuilder.buildUpdateByPrimaryKey(getContext(UserMapper.class.getMethod("updateByPrimaryKey", Object.class))).trim();
        System.out.println(actual);
        String expected = "<script>UPDATE `user`<set>`name` = #{name}, `address` = #{address}, `birth` = #{birth}, `create_time` = #{createTime}, `update_time` = #{updateTime}, </set>WHERE `id` = #{id}</script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildUpdateByPrimaryKeySelective() throws Exception {
        String actual = CrudSqlBuilder.buildUpdateByPrimaryKeySelective(getContext(UserMapper.class.getMethod("updateByPrimaryKeySelective", Object.class))).trim();
        String expected = "<script>UPDATE `user`<set><if test=\"name != null\">`name` = #{name}, </if><if test=\"address != null\">`address` = #{address}, </if><if test=\"birth != null\">`birth` = #{birth}, </if><if test=\"createTime != null\">`create_time` = #{createTime}, </if><if test=\"updateTime != null\">`update_time` = #{updateTime}, </if></set>WHERE `id` = #{id}</script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildUpsert() throws Exception {
        String actual = CrudSqlBuilder.buildUpsert(getContext(UserMapper.class.getMethod("upsert", Object.class))).trim();
        String expected = "<script>INSERT INTO `user`<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \">`id`, `name`, `address`, `birth`, `create_time`, `update_time`, </trim><trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \">#{id}, #{name}, #{address}, #{birth}, #{createTime}, #{updateTime}, </trim> ON DUPLICATE KEY UPDATE <trim suffixOverrides=\", \">`name` = #{name}, `address` = #{address}, `birth` = #{birth}, `create_time` = #{createTime}, `update_time` = #{updateTime}, </trim></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildUpsertSelective() throws Exception {
        String actual = CrudSqlBuilder.buildUpsertSelective(getContext(UserMapper.class.getMethod("upsertSelective", Object.class))).trim();
        String expected = "<script>INSERT INTO `user`<trim prefix=\"(\" suffix=\")\" suffixOverrides=\", \"><if test=\"id != null\">`id`, </if><if test=\"name != null\">`name`, </if><if test=\"address != null\">`address`, </if><if test=\"birth != null\">`birth`, </if><if test=\"createTime != null\">`create_time`, </if><if test=\"updateTime != null\">`update_time`, </if></trim><trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\", \"><if test=\"id != null\">#{id}, </if><if test=\"name != null\">#{name}, </if><if test=\"address != null\">#{address}, </if><if test=\"birth != null\">#{birth}, </if><if test=\"createTime != null\">#{createTime}, </if><if test=\"updateTime != null\">#{updateTime}, </if></trim> ON DUPLICATE KEY UPDATE <trim suffixOverrides=\", \"><if test=\"name != null\">`name` = #{name}, </if><if test=\"address != null\">`address` = #{address}, </if><if test=\"birth != null\">`birth` = #{birth}, </if><if test=\"createTime != null\">`create_time` = #{createTime}, </if><if test=\"updateTime != null\">`update_time` = #{updateTime}, </if></trim></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildDeleteByPrimaryKey() throws Exception {
        String actual = CrudSqlBuilder.buildDeleteByPrimaryKey(getContext(UserMapper.class.getMethod("deleteByPrimaryKey", Object.class))).trim();
        String expected = "DELETE FROM `user` WHERE `id` = #{id}";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildDeleteByPrimaryKeys() throws Exception {
        String actual = CrudSqlBuilder.buildDeleteByPrimaryKeys(getContext(UserMapper.class.getMethod("deleteByPrimaryKeys", List.class))).trim();
        String expected = "<script>DELETE FROM `user` WHERE `id` IN <foreach collection=\"list\" item=\"e\" open=\"(\" separator=\", \"  close=\")\">#{e}</foreach></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildSelectByPrimaryKey() throws Exception {
        String actual = CrudSqlBuilder.buildSelectByPrimaryKey(getContext(UserMapper.class.getMethod("selectByPrimaryKey", Object.class))).trim();
        String expected = "SELECT * FROM `user` WHERE `id` = #{id}";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildExistsByPrimaryKey() throws Exception {
        String actual = CrudSqlBuilder.buildExistsByPrimaryKey(getContext(UserMapper.class.getMethod("existsByPrimaryKey", Object.class))).trim();
        String expected = "SELECT COUNT(0) FROM `user` WHERE `id` = #{id} LIMIT 1";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildSelectOneByExample() throws Exception {
        String actual = CrudSqlBuilder.buildSelectOneByExample(getContext(UserMapper.class.getMethod("selectOneByExample", Object.class))).trim();
        String expected = "<script>SELECT * FROM `user`<where><if test=\"example.address != null\">AND address = #{example.address}</if><if test=\"example.birth != null\">AND birth = #{example.birth}</if><if test=\"example.createTime != null\">AND create_time = #{example.createTime}</if><if test=\"example.id != null\">AND id = #{example.id}</if><if test=\"example.name != null\">AND name = #{example.name}</if><if test=\"example.updateTime != null\">AND update_time = #{example.updateTime}</if></where> LIMIT 1</script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildSelectByExample() throws Exception {
        String actual = CrudSqlBuilder.buildSelectByExample(getContext(UserMapper.class.getMethod("selectByExample", Object.class))).trim();
        String expected = "<script>SELECT * FROM `user`<where><if test=\"example.address != null\">AND address = #{example.address}</if><if test=\"example.birth != null\">AND birth = #{example.birth}</if><if test=\"example.createTime != null\">AND create_time = #{example.createTime}</if><if test=\"example.id != null\">AND id = #{example.id}</if><if test=\"example.name != null\">AND name = #{example.name}</if><if test=\"example.updateTime != null\">AND update_time = #{example.updateTime}</if></where></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildSelectByExampleWithPage() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("example", new User());
        params.put("page", new PageRequest(2, 10));
        String actual = CrudSqlBuilder.buildSelectByExampleWithPage(getContext(UserMapper.class.getMethod("selectByExampleWithPage", Object.class, PageRequest.class)), params).trim();
        String expected = "<script>SELECT * FROM `user`<where><if test=\"example.address != null\">AND address = #{example.address}</if><if test=\"example.birth != null\">AND birth = #{example.birth}</if><if test=\"example.createTime != null\">AND create_time = #{example.createTime}</if><if test=\"example.id != null\">AND id = #{example.id}</if><if test=\"example.name != null\">AND name = #{example.name}</if><if test=\"example.updateTime != null\">AND update_time = #{example.updateTime}</if></where> LIMIT 10,10</script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildCountByExample() throws Exception {
        String actual = CrudSqlBuilder.buildCountByExample(getContext(UserMapper.class.getMethod("countByExample", Object.class))).trim();
        String expected = "<script>SELECT COUNT(0) FROM `user`<where><if test=\"example.address != null\">AND address = #{example.address}</if><if test=\"example.birth != null\">AND birth = #{example.birth}</if><if test=\"example.createTime != null\">AND create_time = #{example.createTime}</if><if test=\"example.id != null\">AND id = #{example.id}</if><if test=\"example.name != null\">AND name = #{example.name}</if><if test=\"example.updateTime != null\">AND update_time = #{example.updateTime}</if></where></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildSelectOneByMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("address", null);
        map.put("birth", null);
        String actual = CrudSqlBuilder.buildSelectOneByMap(getContext(UserMapper.class.getMethod("selectOneByMap", Map.class)), Collections.singletonMap("map", map)).trim();
        String expected = "<script>SELECT * FROM `user`<where><if test=\"map.address != null\">AND address = #{map.address}</if><if test=\"map.birth != null\">AND birth = #{map.birth}</if></where> LIMIT 1</script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildSelectByMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("address", null);
        map.put("birth", null);
        String actual = CrudSqlBuilder.buildSelectByMap(getContext(UserMapper.class.getMethod("selectByMap", Map.class)), Collections.singletonMap("map", map)).trim();
        String expected = "<script>SELECT * FROM `user`<where><if test=\"map.address != null\">AND address = #{map.address}</if><if test=\"map.birth != null\">AND birth = #{map.birth}</if></where></script>";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildCountByMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("address", null);
        map.put("birth", null);
        String actual = CrudSqlBuilder.buildCountByMap(getContext(UserMapper.class.getMethod("countByMap", Map.class)), Collections.singletonMap("map", map)).trim();
        String expected = "<script>SELECT COUNT(0) FROM `user`<where><if test=\"map.address != null\">AND address = #{map.address}</if><if test=\"map.birth != null\">AND birth = #{map.birth}</if></where></script>";
        assertThat(actual).isEqualTo(expected);
    }
}