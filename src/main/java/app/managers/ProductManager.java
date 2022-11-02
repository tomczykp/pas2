package app.managers;

import app.model.Product;
import app.repositories.ProductRepository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProductManager {

	@Inject
	private ProductRepository productRepository;

	public Product create (int price) {
		return productRepository.insert(new Product(price));
	}

	public void delete (int id) throws Exception {
		Product p = productRepository.get(id);
		if (p.getCurrentReservations().isEmpty())
			productRepository.delete(id);
		else
			throw new Exception("cannot delete product with ongoing reservations");
	}

	public Product modify (int id, Function<Product, Product> func) throws Exception {
		return productRepository.modify(id, func);
	}

	public Product get (int id) {
		return productRepository.get(id);
	}

	public Map<Integer, Product> get (Predicate<Product> predicate) {
		try {
			return productRepository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Product> getMap () {
		return productRepository.getMap();
	}

	public int getLength () {
		return productRepository.getLenght();
	}

}
