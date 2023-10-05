package com.jagsii.springxx.test;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.function.Consumer;

@Rollback
@Transactional
@AutoConfigureTestDatabase
@ImportAutoConfiguration({JdbcTemplateAutoConfiguration.class, MybatisAutoConfiguration.class, SqlInitializationAutoConfiguration.class, TransactionAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public abstract class MybatisMapperTests extends SpringTests {
    @Autowired
    protected SqlSessionFactory sqlSessionFactory;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

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

    protected <T> void testMapper(Consumer<T> fn, Class<T> mapperClass) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        if (!configuration.hasMapper(mapperClass)) {
            sqlSessionFactory.getConfiguration().addMapper(mapperClass);
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            fn.accept(sqlSession.getMapper(mapperClass));
        }
    }
}
