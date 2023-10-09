package com.jagsii.springxx;

import com.jagsii.springxx.test.MybatisMapperTests;
import com.jagsii.springxx.test.SpringTests;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@Import({
        SpringTests.TestPropertiesConfig.class,
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DbTests extends MybatisMapperTests {
}
