package com.example.demo.app.endpoints;

import com.example.demo.app.dto.CustomerDTO;
import com.example.demo.app.dto.ReservationDTO;
import com.example.demo.app.exceptions.NotFoundException;
import com.example.demo.app.managers.CustomerManager;
import com.example.demo.app.model.Customer;
import com.example.demo.app.model.Reservation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "/rest/api/customer")
public class CustomerEndpoint {

	private CustomerManager manager;

	@Autowired
	public CustomerEndpoint(CustomerManager customerManager) {
		this.manager = customerManager;
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get (@PathVariable("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));
			return ResponseEntity.ok(new CustomerDTO(customer));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status","customer not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

	@GetMapping(path = "/{id}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getReservations (@PathVariable("id") String id, @RequestParam("past") boolean fromPast) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));

			List<Reservation> res;
			if (fromPast)
				res = customer.getPastReservations();
			else
				res = customer.getFutureReservations();

			return ResponseEntity.ok(mapDTO(res));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "customer not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAll(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "exact", required = false) String exact) {
		Map<Integer, CustomerDTO> data;

		if ( Objects.equals(username, "") || username == null)
			data = manager.getMap();
		else if (exact == null || exact.equals(""))
			data = manager.get(
					(Customer c) -> (c.getUsername().contains(username)) || username.contains(c.getUsername()));
		else
			data = manager.get((Customer c) -> Objects.equals(c.getUsername(), username));

		return ResponseEntity.ok(data);
	}

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity put(@RequestParam(value = "email", required = false) String email,
						@RequestParam(value = "username", required = false) String username,
						@RequestParam(value = "password", required = false) String password) {

		if ( Objects.equals(email, "") || email == null)
			return ResponseEntity.status(404).body(new JSONObject().put("status", "missing arguments 'email'").toString());
		if (Objects.equals(username, "") || username == null)
			return ResponseEntity.status(404).body(new JSONObject().put("status", "missing arguments 'username'").toString());


		try {
			CustomerDTO customer = manager.create(username, email, password);
			return ResponseEntity.ok(customer);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e);
		}
	}

	@PatchMapping(path = "/{id}/activate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity setActive(@PathVariable("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));
			customer.setActive(true);
			return ResponseEntity.status(200).body(new JSONObject().put("status", "set to active").toString());
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "customer not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()));
		}
	}

	private List<ReservationDTO> mapDTO (List<Reservation> reservations) {
		List<ReservationDTO> res = new ArrayList<>();
		for (Reservation r : reservations)
			res.add(new ReservationDTO(r));
		return res;
	}

	@PatchMapping(path = "/{id}/deactivate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity setDeactivate(@PathVariable("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));

			if (!customer.getFutureReservations().isEmpty())
				throw new Exception("cannot deactivate user with ongoing reservations");

			customer.setActive(false);
			return ResponseEntity.status(200).body(new JSONObject().put("status", "set to deactive").toString());
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "customer not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

	@PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity update (@RequestBody CustomerDTO newCustomer) {
		try {
			int t = newCustomer.customerID;
			Customer res = manager.modify(t,
					(Customer current) -> current
							.setActive(newCustomer.active)
							.setEmail(newCustomer.email));

			return ResponseEntity.ok(new CustomerDTO(res));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "customer not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}
}
