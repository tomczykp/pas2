package com.example.demo.app.endpoints;

import com.example.demo.app.dto.ReservationDTO;
import com.example.demo.app.exceptions.DateException;
import com.example.demo.app.exceptions.NotFoundException;
import com.example.demo.app.managers.CustomerManager;
import com.example.demo.app.managers.ProductManager;
import com.example.demo.app.managers.ReservationManager;
import com.example.demo.app.model.Customer;
import com.example.demo.app.model.Product;
import com.example.demo.app.model.Reservation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestController
@RequestMapping(path = "/rest/api/reservation")
public class ReservationEndpoint {

	private ReservationManager reservationManager;

	private CustomerManager customerManager;

	private ProductManager productManager;

    @Autowired
    public ReservationEndpoint(ReservationManager reservationManager, CustomerManager customerManager, ProductManager productManager) {
        this.reservationManager = reservationManager;
        this.customerManager = customerManager;
        this.productManager = productManager;
    }

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity put (
			@RequestParam(value = "sdate", required = false) String start,
			@RequestParam(value = "edate", required = false) String end,
			@RequestParam(value = "cid", required = false) String cid,
			@RequestParam(value = "pid", required = false) String pid) {
		if (Objects.equals(start, "") || start == null)
			return ResponseEntity.status(404).body(new JSONObject().put("status", "missing arguments 'sdate'").toString());
		if (Objects.equals(end, "") || end == null)
			return ResponseEntity.status(404).body(new JSONObject().put("status", "missing arguments 'edate'").toString());
		if (Objects.equals(cid, "") || cid == null)
			return ResponseEntity.status(404).body(new JSONObject().put("status", "missing arguments 'cid'").toString());
		if (Objects.equals(pid, "") || pid == null)
			return ResponseEntity.status(404).body(new JSONObject().put("status", "missing arguments 'pid'").toString());

		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate endDate;
			LocalDate startDate;
			try {
				endDate = LocalDate.parse(end, dateTimeFormatter);
				startDate = LocalDate.parse(start, dateTimeFormatter);
			} catch (DateTimeException e) {
				throw new DateException();
			}
			Customer customer;
			Product product;

			try {
				customer = customerManager.get(Integer.parseInt(cid));
			} catch (NotFoundException e) {
				return ResponseEntity.status(404).body(new JSONObject().put("status", "customer not found").toString());
			}
			try {
				product = productManager.get(Integer.parseInt(pid));
			} catch (NotFoundException e) {
				return ResponseEntity.status(404).body(new JSONObject().put("status", "product not found").toString());
			}
			for (Reservation r : product.getReservations())
				System.out.println("\t " + new ReservationDTO(r));

			ReservationDTO reservation = reservationManager.create(startDate, endDate, customer, product);

			return ResponseEntity.ok(reservation);

		} catch (NumberFormatException | DateException e) {
			return ResponseEntity.status(500).body(e);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAll () {
		return ResponseEntity.ok(reservationManager.getMap());
	}

	@GetMapping(path ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity get (@PathVariable("id") String id) {
		try {
			Reservation reservation = reservationManager.get(Integer.parseInt(id));
			return ResponseEntity.ok(new ReservationDTO(reservation));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "reservation not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

	@DeleteMapping(path ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity delete(@PathVariable("id") String id) {
		try {
			int t = Integer.parseInt(id);
			reservationManager.delete(t);
			return ResponseEntity.ok(new JSONObject().put("status", "deletion succesful").toString());
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "reservation not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

	@PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity update (@RequestBody ReservationDTO newReservation) {
		try {

			int t = newReservation.reservationID;
			Reservation res = reservationManager.modify(t,
					(Reservation current) -> current
							.switchCustomer(customerManager.get(newReservation.customerID))
							.setEndDate(newReservation.endDate)
							.setStartDate(newReservation.startDate)
							.switchProduct(productManager.get(newReservation.productID)));

			return ResponseEntity.ok(new ReservationDTO(res));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(500).body(e);
		} catch (NotFoundException e) {
			return ResponseEntity.status(404).body(new JSONObject().put("status", "customer not found").toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(new JSONObject().put("status", e.getMessage()).toString());
		}
	}

}