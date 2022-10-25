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
	private String email;

	@OneToMany
	private final List<Reservation> reservations = new ArrayList<>();

	public Customer () {}

	public Customer (String u, String e) {
		this.email = e;
		this.username = u;
	}

	public List<Reservation> getReservations () {
		return this.reservations;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (!(o instanceof Customer customer)) return false;

		return new EqualsBuilder().append(getCustomerID(), customer.getCustomerID()).append(getUsername(),
				customer.getUsername()).append(getEmail(), customer.getEmail()).isEquals();
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37).append(getCustomerID()).append(getUsername()).append(getEmail()).toHashCode();
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

	public void setUsername (String username) {
		this.username = username;
	}

	public String getEmail () {
		return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

}
