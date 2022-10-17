package repo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class InMemoryRepository<T> implements Repository<T> {

	private final List<T> lista;

	public InMemoryRepository() {
		this.lista = new ArrayList<>();
	}

	@Override
	public T insert (T o) {
		this.lista.add(o);
		return o;
	}

	@Override
	public T get (Predicate<T> pred) throws Exception {
		for (T t: this.lista)
			if (pred.test(t))
				return t;

		throw new Exception("Not found");
	}

	@Override
	public T modify (T o) {
		if (this.lista.contains(o)) {
			this.lista.remove(o);
			this.lista.add(o);
		}

		return o;
	}

	@Override
	public void delete (T o) {
		this.lista.remove(o);
	}

}
