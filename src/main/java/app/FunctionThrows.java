package app;

@FunctionalInterface
public interface FunctionThrows<V> {

	V apply (V t) throws Exception;

}
