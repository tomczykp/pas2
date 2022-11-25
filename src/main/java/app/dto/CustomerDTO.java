package app.dto;

import app.model.Customer;
import app.model.CustomerType;
import app.model.Reservation;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class CustomerDTO {

	@XmlElement
	public int customerID;
	@XmlElement
	public String email;
	@XmlElement
	public String username;
	@XmlElement
	public List<Integer> reservations;
	@XmlElement
	public boolean active;

	@XmlElement
	public CustomerType type;

	public CustomerDTO (Customer c) {
		email = c.getEmail();
		username = c.getUsername();
		customerID = c.getCustomerID();
		reservations = new ArrayList<>();
		for (Reservation r : c.getReservations())
			reservations.add(r.getReservationID());
		active = c.isActive();
		type = c.getType();
	}

	public CustomerDTO () {}

}
