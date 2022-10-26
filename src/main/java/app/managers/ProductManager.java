package app.managers;

import app.model.a.Product;
import app.repository.InMemoryRepository;
import app.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProductManager {

	private final Repository<Integer, Product> repository;
	private int counter;

	public ProductManager () {
		this.repository = new InMemoryRepository<>();
		this.counter = 0;
	}

	public Product create(int price) {
		return this.repository.insert(this.counter++, new Product(price));
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
