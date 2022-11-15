package com.example.demo.app.repositories;

import com.example.demo.app.FunctionThrows;
import com.example.demo.app.exceptions.NotFoundException;
import java.util.*;
import java.util.function.Predicate;

public abstract class InMemoryRepository<K, V> implements Repository<K, V> {

	private final HashMap<K, V> map;

	public InMemoryRepository () {
		map = new HashMap<>();
	}

	@Override
	public int getLenght () {
		return map.size();
	}

	@Override
	public V get (K k) throws NotFoundException {
		V t = map.get(k);
		if (t == null)
			throw new NotFoundException();
		return t;
	}

	@Override
	public HashMap<K, V> getMap () {
		return map;
	}

	@Override
	public V insert (K k, V v) {
		map.put(k, v);
		return v;
	}

	@Override
	public Map<K, V> get (Predicate<V> pred) {
		Map<K, V> res = new HashMap<>();
		for (Map.Entry<K, V> t : map.entrySet())
			if (pred.test(t.getValue()))
				res.put(t.getKey(), t.getValue());

		return res;
	}

	@Override
	public V modify (K k, FunctionThrows<V> func) throws Exception {
		if (map.containsKey(k)) {
			V v = map.get(k);
			return func.apply(v);
		}
		throw new NotFoundException();
	}


	@Override
	public void delete (K k) {
		map.remove(k);
	}
}
