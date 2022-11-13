package app.dto;

import app.model.Customer;
import app.model.Reservation;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class CustomerDTO {

	@XmlElement
	public final int customerID;
	@XmlElement
	public final String email;
	@XmlElement
	public final String username;
	@XmlElement
	public final List<Integer> reservations;
	@XmlElement
	public final boolean active;
	
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
