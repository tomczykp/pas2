package app.managers;

import app.dto.ProductDTO;
import app.exceptions.NotFoundException;
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

	public ProductDTO create (int price) {
		return new ProductDTO(productRepository.insert(new Product(price)));
	}

	public void delete (int id) throws Exception {
		Product p = productRepository.get(id);
		if (!p.isReserved() && p.getFutureReservations().isEmpty())
			productRepository.delete(id);
		else
			throw new Exception("cannot delete product with ongoing reservations");
	}

	public Product modify (int id, Function<Product, Product> func) throws NotFoundException {
		return productRepository.modify(id, func);
	}

	public Product get (int id) throws NotFoundException {
		return productRepository.get(id);
	}

	public Map<Integer, ProductDTO> get (Predicate<Product> predicate) {
		try {
			HashMap<Integer, ProductDTO> res = new HashMap<>();
			for (Map.Entry<Integer, Product> entry : productRepository.get(predicate).entrySet())
				res.put(entry.getKey(), new ProductDTO(entry.getValue()));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, ProductDTO> getMap () {
		HashMap<Integer, ProductDTO> res = new HashMap<>();
		for (Map.Entry<Integer, Product> entry : productRepository.getMap().entrySet())
			res.put(entry.getKey(), new ProductDTO(entry.getValue()));
		return res;
	}

	public int getLength () {
		return productRepository.getLenght();
	}

}
