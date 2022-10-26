package app.managers.a;

import app.model.a.Product;
import app.repositories.InMemoryRepository;
import app.repositories.ProductRepository;
import app.repositories.Repository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProductManager {

	@Inject
	private ProductRepository repository;

	public Product create(int price) {
		return this.repository.insert(new Product(price));
	}

	public void delete(int id) {
		this.repository.delete(id);
	}

	public Product modify(int id, Function<Product, Product> func) throws Exception {
		return this.repository.modify(id, func);
	}

	public Product get(int id) {
		return this.repository.get(id);
	}

	public List<Product> get (Predicate<Product> predicate) {
		try {
			return this.repository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Product> getMap() {
		return this.repository.getMap();
	}

	public int getLength() {
		return this.repository.getLenght();
	}

}
