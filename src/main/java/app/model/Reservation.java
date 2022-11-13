package app.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

public class Reservation {

	private int reservationID;

	public int getReservationID () {
		return reservationID;
	}

	private LocalDate startDate;

	private LocalDate endDate;

	public Customer getCustomer () {
		return customer;
	}

	public void setId (int id) {
		reservationID = id;
	}

	public Product getProduct () {
		return product;
	}


	private Customer customer;

	private Product product;

	public Reservation (LocalDate startDate, LocalDate endDate, Customer customer, Product product) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.customer = customer;
		this.product = product;
		product.addReservation(this);
		customer.addReservation(this);
	}

	public Reservation () {}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37)
				.append(reservationID)
				.append(startDate)
				.append(endDate)
				.append(customer)
				.append(product).toHashCode();
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (o.getClass() != getClass()) return false;
		Reservation that = (Reservation) o;

		return new EqualsBuilder()
				.append(reservationID, that.reservationID)
				.append(startDate, that.startDate)
				.append(endDate, that.endDate)
				.append(customer, that.customer)
				.append(product, that.product).isEquals();
	}

	public LocalDate getStartDate () {
		return startDate;
	}

	public LocalDate getEndDate () {
		return endDate;
	}

}