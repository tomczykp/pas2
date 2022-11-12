package app.repositories;


import app.exceptions.NotFoundException;

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
	public V get(K k) throws NotFoundException {
		V t = lista.get(k);
		if (t == null)
			throw new NotFoundException();
		return t;
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
	public V modify (K k, Function<V, V> func) throws NotFoundException {
		if (lista.containsKey(k)) {
			V v = lista.get(k);
			return func.apply(v);
		}
		throw new NotFoundException();
	}


	@Override
	public void delete (K k) {
		lista.remove(k);
	}
}
