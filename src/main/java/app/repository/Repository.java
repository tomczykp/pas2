package app.repository;

import java.util.HashMap;
import java.util.function.Predicate;

public interface Repository<K, V> {

	V insert (K k, V v);
	V get (Predicate<V> pred) throws Exception;
	V modify (K k);
	int getLenght();
	void delete(K k);
	HashMap<K, V> getMap();
	V get(K k);
}
