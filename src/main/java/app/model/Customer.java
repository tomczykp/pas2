package app.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "customer_id", nullable = false)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int customerID;

	@Column(unique = true)
	@NotNull
	private String username;

	@Column
	@NotNull
	private String password;

	@Column
	private String email;

	@Column
	private boolean isActive;

	@OneToMany
	private final List<Reservation> reservations = new ArrayList<>();

	public Customer () {}

	public Customer (String u, String e, String p) {
		email = e;
		username = u;
		password = p;
		isActive = true;
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
				.append(username, customer.username)
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

	public void setCustomerID (int customerID) {
		this.customerID = customerID;
	}

	public String getUsername () {
		return username;
	}

	public Customer setPassword (String password) {
		this.password = password;
		return this;
	}

	public void setUsername (String username) {
		this.username = username;
	}

	public String getEmail () {
		return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

	public boolean isActive () {
		return isActive;
	}

	public void setActive (boolean active) {
		isActive = active;
	}

}
