package app.dto;

import app.model.Customer;
import app.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class CustomerDTO {

	public String getEmail () {
		return email;
	}

	public String getUsername () {
		return username;
	}

	public List<ReservationDTO> getReservations () {
		return reservations;
	}

	public long getCustomerID () {
		return customerID;
	}

	private final long customerID;
	private final String email;
	private final String username;
	private final List<ReservationDTO> reservations;

	public CustomerDTO(Customer c) {
		email = c.getEmail();
		username = c.getUsername();
		customerID = c.getCustomerID();
		reservations = new ArrayList<>();
		for (Reservation r: c.getReservations())
			reservations.add(new ReservationDTO(r));
	}
}
