package app.repositories;


import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class InMemoryRepository<K, V> implements Repository<K, V> {

	private final HashMap<K, V> lista;

	public InMemoryRepository() {
		lista = new HashMap<>();
	}

	@Override
	public int getLenght() {
		return lista.size();
	}

	@Override
	public V get(K k) {
		return lista.get(k);
	}

	private V getObj(V v) throws Exception {
		return get((item) -> item.equals(v)).get(0);
	}

	@Override
	public HashMap<K, V> getMap() {
		return lista;
	}

	@Override
	public V insert (K k, V v) {
		lista.put(k, v);
		return v;
	}

	@Override
	public Map<K, V> get (Predicate<V> pred) {
		Map<K, V> res = new HashMap<>();
		for (Map.Entry<K, V> t : lista.entrySet())
			if (pred.test(t.getValue()))
				res.put(t.getKey(), t.getValue());

		return res;
	}

	@Override
	public V modify (K k, Function<V, V> func) throws Exception {
		if (lista.containsKey(k)) {
			V v = lista.get(k);
			return func.apply(v);
		}
		throw new Exception("Object with this id not found" + k.toString());
	}

	@Override
	public void delete (K k) {
		lista.remove(k);
	}
}
