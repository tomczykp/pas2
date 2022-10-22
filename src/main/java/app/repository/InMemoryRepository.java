package app.repository;

import jakarta.persistence.Id;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class InMemoryRepository<K, V> implements Repository<K, V> {

	private final HashMap<K, V> lista;

	public InMemoryRepository() {
		this.lista = new HashMap<>();
	}

	@Override
	public int getLenght() {
		return this.lista.size();
	}

	@Override
	public V get(K k) {
		return this.lista.get(k);
	}

	private V getObj(V v) throws Exception {
		return this.get((item) -> item.equals(v)).get(0);
	}

	@Override
	public HashMap<K, V> getMap() {
		return this.lista;
	}

	@Override
	public V insert (K k, V v) {
		this.lista.put(k, v);
		return v;
	}

	@Override
	public List<V> get (Predicate<V> pred) {
		List<V> res = new ArrayList<>();
		for (Map.Entry<K, V> t : this.lista.entrySet())
			if (pred.test(t.getValue()))
				res.add(t.getValue());

		return res;
	}

	@Override
	public V modify (K k, Function<V, V> func) throws Exception {
		if (this.lista.containsKey(k)) {
			V v = this.lista.get(k);
			return func.apply(v);
		}
		throw new Exception("Object with this id not found" + k.toString());
	}

	@Override
	public void delete (K k) {
		this.lista.remove(k);
	}
}
