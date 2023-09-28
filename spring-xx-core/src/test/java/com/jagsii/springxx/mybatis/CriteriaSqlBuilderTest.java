package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.mybatis.enums.OrderDirection;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class CriteriaSqlBuilderTest extends SqlBuilderTests {
    interface UserMapper extends CriteriaBaseMapper<User> {

    }

    @Test
    void buildCountByCriteria() throws Exception {
        QueryCriteria<User> criteria = Criterion.query(User.class).eq("id", 1L);
        String actual = CriteriaSqlBuilder.buildCountByCriteria(getContext(UserMapper.class.getMethod("countByCriteria", Criteria.class)), Collections.singletonMap("criteria", criteria));
        String expected = "SELECT COUNT(0) FROM `user` WHERE `id` = #{criteria.params.param1}";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildSelectByCriteria() throws Exception {
        QueryCriteria<User> criteria = Criterion.query(User.class).eq("id", 1L).orderBy("create_time", OrderDirection.DESC).orderBy("update_time", OrderDirection.ASC).limit(10, 20);
        String actual = CriteriaSqlBuilder.buildSelectByCriteria(getContext(UserMapper.class.getMethod("selectByCriteria", Criteria.class)), Collections.singletonMap("criteria", criteria));
        String expected = "SELECT * FROM `user` WHERE `id` = #{criteria.params.param1} ORDER BY `create_time` DESC, `update_time` ASC LIMIT 10,20";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildUpdateByCriteria() throws Exception {
        UpdateCriteria<User> criteria = Criterion.update(User.class).set("name", "Fish").eq("id", 1L);
        String actual = CriteriaSqlBuilder.buildUpdateByCriteria(getContext(UserMapper.class.getMethod("updateByCriteria", Criteria.class)), Collections.singletonMap("criteria", criteria));
        String expected = "UPDATE `user` SET `name` = #{criteria.params.param1} WHERE `id` = #{criteria.params.param2}";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void buildDeleteByCriteria() throws Exception {
        QueryCriteria<User> criteria = Criterion.query(User.class).eq("id", 1L);
        String actual = CriteriaSqlBuilder.buildDeleteByCriteria(getContext(UserMapper.class.getMethod("deleteByCriteria", Criteria.class)), Collections.singletonMap("criteria", criteria));
        String expected = "DELETE FROM `user` WHERE `id` = #{criteria.params.param1}";
        assertThat(actual).isEqualTo(expected);
    }
}