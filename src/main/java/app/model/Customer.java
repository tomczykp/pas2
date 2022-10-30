package app.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "customer_id", nullable = false)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private Long customerID;

	@Column(unique = true)
	@NotNull
	private String username;

	@Column
	@NotNull
	private String password;

	@Column
	private String email;

	@OneToMany
	private final List<Reservation> reservations = new ArrayList<>();

	public Customer () {}

	public Customer (String u, String e, String p) {
		email = e;
		username = u;
		password = p;
	}

	public List<Reservation> getReservations () {
		return reservations;
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

	public Long getCustomerID () {
		return customerID;
	}

	public void setCustomerID (Long customerID) {
		this.customerID = customerID;
	}

	public String getUsername () {
		return username;
	}

	public Customer setPassword(String password) {
		this.password = password; return this;
	}

	public Customer setUsername (String username) {
		this.username = username; return this;
	}

	public String getEmail () {
		return email;
	}

	public Customer setEmail (String email) {
		this.email = email;
		return this;
	}

}
