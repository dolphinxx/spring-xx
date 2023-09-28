package com.jagsii.springxx.mybatis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LambdaQueryCriteriaTest {

    @Test
    void columnToString() {
        LambdaQueryCriteria<User> criteria = new LambdaQueryCriteria<>(User.class);
        assertThat(criteria.columnToString(User::getId)).isEqualTo("id");
        assertThat(criteria.columnToString(User::getCreateTime)).isEqualTo("create_time");
    }
}