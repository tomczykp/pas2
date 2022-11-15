package com.example.demo.app.endpoints;

import com.example.demo.app.dto.ProductDTO;
import com.example.demo.app.dto.ReservationDTO;
import com.example.demo.app.exceptions.NotFoundException;
import com.example.demo.app.managers.ProductManager;
import com.example.demo.app.model.Product;
import com.example.demo.app.model.Reservation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/rest/api/product")
public class ProductEndpoint {

	private ProductManager manager;

    @Autowired
    public ProductEndpoint(ProductManager productManager) {
        this.manager = productManager;
    }

	/*
	GIT
	 */
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity put(@RequestParam(value = "price", required = false) String p) {
		if (Objects.equals(p, "") || p == null)
			return ResponseEntity.status(404).body(new JSONObject().put("status", "missing parameter 'price'").toString());
		try {
			int t = Integer.parseInt(p);
			ProductDTO product = manager.create(t);
			return ResponseEntity.ok(product);
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		}
	}

	/*
	GIT
	 */
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity get (@PathVariable("id") String id) {
		try {
			Product product = manager.get(Integer.parseInt(id));
			return ResponseEntity.ok(new ProductDTO(product));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "product not found").toString());
		}
	}

	private List<ReservationDTO> mapDTO (List<Reservation> reservations) {
		List<ReservationDTO> res = new ArrayList<>();
		for (Reservation r : reservations)
			res.add(new ReservationDTO(r));
		return res;
	}


	/*
	GIT
	 */
	@GetMapping(path = "/{id}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getReservations (@PathVariable("id") String id, @RequestParam("past") boolean fromPast) {
		try {
			Product product = manager.get(Integer.parseInt(id));
			List<Reservation> res;

			if (fromPast)
				res = product.getPastReservations();
			else
				res = product.getFutureReservations();

			return ResponseEntity.ok(mapDTO(res));

		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "product not found").toString());
		}
	}

	/*
	GIT
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAll() {
		return ResponseEntity.ok(manager.getMap());
	}

	/*
	GIT
	 */
	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity delete(@PathVariable("id") String id) {
		try {
			int t = Integer.parseInt(id);
			manager.delete(t);
			return ResponseEntity.ok(new JSONObject().put("status", "deletion succesful").toString());

		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "product not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

	@PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity update (@RequestBody ProductDTO p) {
		try {

			int t = p.productID;
			Product res = manager.modify(t, (Product p1) -> p1.setPrice(p.price));

			return ResponseEntity.ok(new ProductDTO(res));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "product not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}
}