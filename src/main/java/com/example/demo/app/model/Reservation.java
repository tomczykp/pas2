package com.example.demo.app.model;

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

	public Reservation setId (int id) {
		reservationID = id;
		return this;
	}

	public Product getProduct () {
		return product;
	}

	public Reservation setStartDate (LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	private Customer customer;

	public Reservation setEndDate (LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public Reservation setCustomer (Customer customer) {
		this.customer = customer;
		return this;
	}

	public Reservation setProduct (Product product) {
		this.product = product;
		return this;
	}
	private Product product;

	public Reservation switchCustomer (Customer nCustomer) {
		Customer current = customer;
		nCustomer.getReservations().remove(this);
		current.getReservations().add(this);
		customer = nCustomer;
		return this;
	}

	private boolean overlapingReservations (Product p) {
		for (Reservation r : p.getFutureReservations())
			if (r.startDate.isEqual(startDate) || r.endDate.isEqual(endDate)
					|| (r.startDate.isAfter(startDate) && r.startDate.isBefore(endDate))
					|| (r.endDate.isAfter(startDate) && r.endDate.isBefore(endDate))
					|| (r.startDate.isBefore(startDate) && r.endDate.isAfter(endDate)))
				return true;
		return false;
	}

	public Reservation switchProduct (Product p) throws Exception {

		if (overlapingReservations(p))
			throw new Exception("cannot switch product, already reserved at this period");

		Product curr = product;
		curr.getReservations().remove(this);
		p.getReservations().add(this);
		product = p;
		return this;
	}

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