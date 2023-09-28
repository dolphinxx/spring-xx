package com.jagsii.springxx.mybatis;

import org.assertj.core.data.MapEntry;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractCriteriaTest {
    @Test
    void spec() {
        AbstractCriteria<User, String, QueryCriteria<User>> criteria;
        String sql;
        criteria = new QueryCriteria<>(User.class);
        sql = criteria.eq("name", "Foo").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(sql).isEqualTo("`name` = #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.eqSql("name", "\"Foo\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` = \"Foo\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.ne("name", "Foo").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(sql).isEqualTo("`name` <> #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.neSql("name", "\"Foo\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` <> \"Foo\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.gt("name", "Foo").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(sql).isEqualTo("`name` > #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.gtSql("name", "\"Foo\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` > \"Foo\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.gte("name", "Foo").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(sql).isEqualTo("`name` >= #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.gteSql("name", "\"Foo\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` >= \"Foo\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.lt("name", "Foo").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(sql).isEqualTo("`name` < #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.ltSql("name", "\"Foo\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` < \"Foo\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.lte("name", "Foo").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(sql).isEqualTo("`name` <= #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.lteSql("name", "\"Foo\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` <= \"Foo\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.isNull("name").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` IS NULL");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.isNotNull("name").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` IS NOT NULL");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.in("name", Lists.list("Foo", "Fish")).buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(criteria.getParams()).containsEntry("param2", "Fish");
        assertThat(sql).isEqualTo("`name` IN (#{criteria.params.param1}, #{criteria.params.param2})");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.in("name", "Foo", "Fish").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(criteria.getParams()).containsEntry("param2", "Fish");
        assertThat(sql).isEqualTo("`name` IN (#{criteria.params.param1}, #{criteria.params.param2})");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.inSql("name", "\"Foo\",\"Fish\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` IN (\"Foo\",\"Fish\")");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notIn("name", Lists.list("Foo", "Fish")).buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(criteria.getParams()).containsEntry("param2", "Fish");
        assertThat(sql).isEqualTo("`name` NOT IN (#{criteria.params.param1}, #{criteria.params.param2})");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notIn("name", "Foo", "Fish").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "Foo");
        assertThat(criteria.getParams()).containsEntry("param2", "Fish");
        assertThat(sql).isEqualTo("`name` NOT IN (#{criteria.params.param1}, #{criteria.params.param2})");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notInSql("name", "\"Foo\",\"Fish\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` NOT IN (\"Foo\",\"Fish\")");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.like("name", "%Foo%").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "%\\%Foo\\%%");
        assertThat(sql).isEqualTo("`name` LIKE #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.startsWith("name", "%Foo%").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "\\%Foo\\%%");
        assertThat(sql).isEqualTo("`name` LIKE #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.endsWith("name", "%Foo%").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "%\\%Foo\\%");
        assertThat(sql).isEqualTo("`name` LIKE #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.likeSql("name", "\"%Foo%\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` LIKE \"%Foo%\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notContains("name", "%Foo%").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "%\\%Foo\\%%");
        assertThat(sql).isEqualTo("`name` NOT LIKE #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notStartsWith("name", "%Foo%").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "\\%Foo\\%%");
        assertThat(sql).isEqualTo("`name` NOT LIKE #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notEndsWith("name", "%Foo%").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "%\\%Foo\\%");
        assertThat(sql).isEqualTo("`name` NOT LIKE #{criteria.params.param1}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notLikeSql("name", "\"%Foo%\"").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`name` NOT LIKE \"%Foo%\"");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.between("name", "A", "C").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "A");
        assertThat(criteria.getParams()).containsEntry("param2", "C");
        assertThat(sql).isEqualTo("`name` BETWEEN #{criteria.params.param1} AND #{criteria.params.param2}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.betweenSql("birth", "DATE(\"2000-01-01 01:01:01\")", "DATE(\"2000-01-03 01:01:01\")").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`birth` BETWEEN DATE(\"2000-01-01 01:01:01\") AND DATE(\"2000-01-03 01:01:01\")");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notBetween("name", "A", "C").buildSql();
        assertThat(criteria.getParams()).containsEntry("param1", "A");
        assertThat(criteria.getParams()).containsEntry("param2", "C");
        assertThat(sql).isEqualTo("`name` NOT BETWEEN #{criteria.params.param1} AND #{criteria.params.param2}");

        criteria = new QueryCriteria<>(User.class);
        sql = criteria.notBetweenSql("birth", "DATE(\"2000-01-01 01:01:01\")", "DATE(\"2000-01-03 01:01:01\")").buildSql();
        assertThat(criteria.getParams()).isEmpty();
        assertThat(sql).isEqualTo("`birth` NOT BETWEEN DATE(\"2000-01-01 01:01:01\") AND DATE(\"2000-01-03 01:01:01\")");
    }

    @Test
    void normal() {
        QueryCriteria<User> criteria = new QueryCriteria<>(User.class);
        criteria.like("address", "bc").in("id", 1L, 2L).and().sub().eq("name", "Foo").or().isNull("name");
        String sql = criteria.buildSql();
        assertThat(criteria.getParams()).contains(
                MapEntry.entry("param1", "%bc%"),
                MapEntry.entry("param2", 1L),
                MapEntry.entry("param3", 2L),
                MapEntry.entry("param4", "Foo")
        );
        assertThat(sql).isEqualTo("`address` LIKE #{criteria.params.param1} AND `id` IN (#{criteria.params.param2}, #{criteria.params.param3}) AND (`name` = #{criteria.params.param4} OR `name` IS NULL)");
    }
}