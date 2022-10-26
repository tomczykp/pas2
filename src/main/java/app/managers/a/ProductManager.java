package app.managers.a;

import app.model.a.Product;
import app.repositories.ProductRepository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProductManager {

	@Inject
	private ProductRepository productRepository;

	public Product create(int price) {
		return this.productRepository.insert(new Product(price));
	}

	public void delete(int id) {
		this.productRepository.delete(id);
	}

	public Product modify(int id, Function<Product, Product> func) throws Exception {
		return this.productRepository.modify(id, func);
	}

	public Product get(int id) {
		return this.productRepository.get(id);
	}

	public List<Product> get (Predicate<Product> predicate) {
		try {
			return this.productRepository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Product> getMap() {
		return this.productRepository.getMap();
	}

	public int getLength() {
		return this.productRepository.getLenght();
	}

}
