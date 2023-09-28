package com.jagsii.springxx.mybatis;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


class UpdateCriteriaTest {
    @Test
    void test() {
        UpdateCriteria<User> criteria = new UpdateCriteria<>(User.class);
        criteria.set("id", 1L);
        criteria.set("name", "Foo");
        criteria.set("birth", LocalDate.parse("2000-01-01"));
        String actual = criteria.buildSetsSql();
        assertThat(criteria.getParams()).contains(
                MapEntry.entry("param1", 1L),
                MapEntry.entry("param2", "Foo"),
                MapEntry.entry("param3", LocalDate.parse("2000-01-01"))
        );
        assertThat(actual).isEqualTo("`id` = #{criteria.params.param1}, `name` = #{criteria.params.param2}, `birth` = #{criteria.params.param3}");
    }

}