package app.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Customer {

	@XmlElement
	private String username;

	@XmlElement
	private String password;

	@XmlElement
	private CustomerType type;

	@XmlElement
	private int customerID;

	@XmlElement
	private String email;

	@XmlElement
	private boolean isActive;

	@XmlElement
	private final List<Reservation> reservations = new ArrayList<>();

	public Customer () {}

	public Customer (String u, String e, String p) {
		email = e;
		isActive = true;
		username = u;
		password = p;
		type = CustomerType.CUSTOMER;
	}

	public List<Reservation> getReservations () {
		return reservations;
	}

	public List<Reservation> getFutureReservations () {
		List<Reservation> reservationList = new ArrayList<>();
		for (Reservation r : reservations)
			if (r.getEndDate().isAfter(LocalDate.now()))
				reservationList.add(r);
		return reservationList;
	}

	public List<Reservation> getPastReservations () {
		List<Reservation> reservationList = new ArrayList<>();
		for (Reservation r : reservations)
			if (r.getEndDate().isBefore(LocalDate.now()))
				reservationList.add(r);
		return reservationList;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (!(o instanceof Customer customer)) return false;

		return new EqualsBuilder()
				.append(customerID, customer.customerID)
				.append(username, customer.getUsername())
				.append(email, customer.email).isEquals();
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37)
				.append(customerID)
				.append(username)
				.append(email).toHashCode();
	}

	public void addReservation (Reservation reservation) {
		reservations.add(reservation);
	}

	public int getCustomerID () {
		return customerID;
	}

	public Customer setCustomerID (int customerID) {
		this.customerID = customerID;
		return this;
	}

	public String getEmail () {
		return email;
	}

	public Customer setEmail (String email) {
		this.email = email;
		return this;
	}

	public boolean isActive () {
		return isActive;
	}

	public Customer setActive (boolean active) {
		isActive = active;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public CustomerType getType() {
		return type;
	}

	public void setType(CustomerType type) {
		this.type = type;
	}
}
