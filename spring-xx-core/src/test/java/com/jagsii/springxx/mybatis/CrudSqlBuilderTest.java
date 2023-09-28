package com.jagsii.springxx.mybatis;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    void buildSelectByPrimaryKey() throws Exception {
        String actual = CrudSqlBuilder.buildSelectByPrimaryKey(getContext(UserMapper.class.getMethod("selectByPrimaryKey", Object.class))).trim();
        System.out.println(actual);
        String expected = "SELECT * FROM `user` WHERE `id` = #{id}";
        assertThat(actual).isEqualTo(expected);
    }
}