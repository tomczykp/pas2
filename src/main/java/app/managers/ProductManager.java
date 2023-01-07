package app.managers;

import app.FunctionThrows;
import app.auth.JwsProvider;
import app.dto.ProductDTO;
import app.exceptions.InvalidJWSException;
import app.exceptions.NotFoundException;
import app.model.Product;
import app.repositories.ProductRepository;
import com.nimbusds.jose.JOSEException;
import jakarta.inject.Inject;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ProductManager {

	@Inject
	public ProductRepository productRepository;

	private JwsProvider jwsProvider = new JwsProvider();

	synchronized public ProductDTO create (Product product) {
		return new ProductDTO(productRepository.insert(product));
	}

	public void delete (int id) throws Exception {
		Product p = productRepository.get(id);
		if (!p.isReserved() && p.getFutureReservations().isEmpty())
			productRepository.delete(id);
		else
			throw new Exception("cannot delete product with ongoing reservations");
	}

	public Product modify (int id, FunctionThrows<Product> func, String jws, ProductDTO productDTO) throws Exception {
		if (this.jwsProvider.verify(jws)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", productDTO.productID);
			String newJwt = this.jwsProvider.generateJws(jsonObject.toString());
			if (newJwt.equals(jws)) {
				return productRepository.modify(id, func);
			}
		}
		throw new InvalidJWSException();
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

	public String getJwsFromProduct(int id) throws NotFoundException, JOSEException {
		Product product = productRepository.get(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", product.getProductID());
		return jwsProvider.generateJws(jsonObject.toString());
	}
}
