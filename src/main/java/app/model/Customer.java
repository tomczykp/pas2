package app.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Customer extends User {
	@XmlElement
	private String email;
	@XmlElement
	private boolean isActive;
	@XmlElement
	private final List<Reservation> reservations = new ArrayList<>();

	public Customer () {}

	public Customer (String u, String e, String p) {
		super(u, p, CustomerType.CUSTOMER);
		email = e;
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
				.append(super.getUserID(), customer.getUserID())
				.append(super.getUsername(), customer.getUsername())
				.append(email, customer.email).isEquals();
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37)
				.append(super.getUserID())
				.append(super.getUsername())
				.append(email).toHashCode();
	}

	public void addReservation (Reservation reservation) {
		reservations.add(reservation);
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
}
