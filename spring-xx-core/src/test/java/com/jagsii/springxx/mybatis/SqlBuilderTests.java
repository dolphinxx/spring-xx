package com.jagsii.springxx.mybatis;

import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class SqlBuilderTests {
    protected ProviderContext getContext(Method method) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<ProviderContext> constructor = ProviderContext.class.getDeclaredConstructor(Class.class, Method.class, String.class);
        constructor.setAccessible(true);
        return constructor.newInstance(CrudSqlBuilderTest.UserMapper.class, method, null);
    }
}
