package com.example.demo.app.managers;

import com.example.demo.app.FunctionThrows;
import com.example.demo.app.dto.ProductDTO;
import com.example.demo.app.exceptions.NotFoundException;
import com.example.demo.app.model.Product;
import com.example.demo.app.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class ProductManager {

	private ProductRepository productRepository;

	@Autowired
	public ProductManager(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

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

	public Product modify (int id, FunctionThrows<Product> func) throws Exception {
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
