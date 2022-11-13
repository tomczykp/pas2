package app.dto;

import app.model.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude
public class CustomerDTO {

	public String getEmail () {
		return email;
	}

	public String getUsername () {
		return username;
	}

	public int getReservations () {
		return reservations;
	}

	public long getCustomerID () {
		return customerID;
	}

	private final long customerID;
	private final String email;
	private final String username;
	private final int reservations;
	private final boolean active;

	public boolean isActive () {
		return active;
	}

	public CustomerDTO (Customer c) {
		email = c.getEmail();
		username = c.getUsername();
		customerID = c.getCustomerID();
		reservations = c.getReservations().size();
		active = c.isActive();
	}

}
