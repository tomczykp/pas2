package repo;

import java.util.function.Predicate;

public interface Repository<T> {

	T insert (T t);
	T get (Predicate<T> pred) throws Exception;
	T modify (T t);
	void delete(T t);

}
