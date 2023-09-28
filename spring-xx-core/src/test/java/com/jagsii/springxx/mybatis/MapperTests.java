package com.jagsii.springxx.mybatis;

import com.jagsii.springxx.SpringTests;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase
@ImportAutoConfiguration({JdbcTemplateAutoConfiguration.class, MybatisAutoConfiguration.class, SqlInitializationAutoConfiguration.class})
public abstract class MapperTests extends SpringTests {
    @Autowired
    protected SqlSessionFactory sqlSessionFactory;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeAll
    void beforeAll() {
        sqlSessionFactory.getConfiguration().addMapper(getMapperClass());
    }

    protected abstract Class<?> getMapperClass();

    protected Long insertForGeneratedId(String sql, Object... params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sql);
        pscf.setReturnGeneratedKeys(true);
        for (int i = 0, len = params.length; i < len; i++) {
            pscf.addParameter(new SqlParameter(SqlTypeValue.TYPE_UNKNOWN));
        }
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(params);
        jdbcTemplate.update(psc, keyHolder);
        return ((Number) Objects.requireNonNull(keyHolder.getKeys()).get("id")).longValue();
    }
}
