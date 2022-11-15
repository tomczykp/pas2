package com.example.demo.app;

@FunctionalInterface
public interface FunctionThrows<V> {

	V apply (V t) throws Exception;

}
