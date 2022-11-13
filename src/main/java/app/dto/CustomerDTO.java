package app.dto;

import app.model.Customer;
import app.model.Reservation;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude
public class CustomerDTO {

	public String getEmail () {
		return email;
	}

	public String getUsername () {
		return username;
	}

	public List<Integer> getReservations () {
		return reservations;
	}

	public int getCustomerID () {
		return customerID;
	}

	private final int customerID;
	private final String email;
	private final String username;
	private final List<Integer> reservations;
	private final boolean active;

	public boolean isActive () {
		return active;
	}

	public CustomerDTO (Customer c) {
		email = c.getEmail();
		username = c.getUsername();
		customerID = c.getCustomerID();
		reservations = new ArrayList<>();
		for (Reservation r : c.getReservations())
			reservations.add(r.getReservationID());
		active = c.isActive();
	}

}
