package com.jagsii.springxx.common;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface SerializableFunction<P, R> extends Function<P, R>, Serializable {
}
