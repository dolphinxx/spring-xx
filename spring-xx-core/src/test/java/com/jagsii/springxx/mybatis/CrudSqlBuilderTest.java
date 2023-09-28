package com.jagsii.springxx.mybatis;

import org.junit.jupiter.api.Test;

import java.util.List;

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
        System.out.println(actual);
        String expected = "SELECT * FROM `user` WHERE `id` = #{id}";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildExistsByPrimaryKey() throws Exception {
        String actual = CrudSqlBuilder.buildExistsByPrimaryKey(getContext(UserMapper.class.getMethod("existsByPrimaryKey", Object.class))).trim();
        System.out.println(actual);
        String expected = "SELECT COUNT(0) FROM `user` WHERE `id` = #{id} LIMIT 1";
        assertThat(actual).isEqualTo(expected);
    }
}