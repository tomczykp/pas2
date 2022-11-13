package app.repositories;

import app.FunctionThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface Repository<K, V> {

	// CRUD methods
	V insert (K k, V v);

	V insert (V v) throws Exception;

	Map<K, V> get (Predicate<V> pred);

	V get (K k) throws Exception;

	V modify (K k, FunctionThrows<V> func) throws Exception;

	void delete (K k);

	// utility methods
	int getLenght ();

	HashMap<K, V> getMap ();

}
