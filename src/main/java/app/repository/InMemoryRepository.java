package app.repository;

import jakarta.persistence.Id;

import java.util.HashMap;
import java.util.Map;
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
	public V get (Predicate<V> pred) throws Exception {
		for (Map.Entry<K, V> t : this.lista.entrySet()) {
			if (pred.test(t.getValue()))
				return t.getValue();
		}
		throw new Exception("Not found");
	}

	@Override
	public V modify (K k) {
		V obj = this.lista.get(k);
		if (this.lista.containsKey(k)) {
			this.lista.remove(k);
			this.lista.put(k, obj);
		}
		return obj;
	}

	@Override
	public void delete (K k) {
		this.lista.remove(k);
	}
}
