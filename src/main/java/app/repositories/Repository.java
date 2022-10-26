package app.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Repository<K, V> {

	// CRUD methods
	V insert (K k, V v);
	List<V> get (Predicate<V> pred);
	V get(K k);
	V modify (K k, Function<V, V> func) throws Exception;
	void delete(K k);

	// utility methods
	int getLenght();
	HashMap<K, V> getMap();
}
