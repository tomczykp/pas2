package com.example.demo.app.dto;

import com.example.demo.app.model.Customer;
import com.example.demo.app.model.Reservation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerDTO implements Serializable {

	public int customerID;

	public String email;

	public String username;

	public List<Integer> reservations;

	public boolean active;

	public CustomerDTO (Customer c) {
		email = c.getEmail();
		username = c.getUsername();
		customerID = c.getCustomerID();
		reservations = new ArrayList<>();
		for (Reservation r : c.getReservations())
			reservations.add(r.getReservationID());
		active = c.isActive();
	}

	public CustomerDTO () {}

}
