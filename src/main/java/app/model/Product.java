package app.model;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Product {

	private int productID;
	private long price;

	private final List<Reservation> reservations = new ArrayList<>();

	public Product (long price) {
		this.price = price;
	}

	public Product () {}

	public int getProductID () {
		return productID;
	}

	public Product setId (int id) {
		productID = id;
		return this;
	}

	public long getPrice () {
		return price;
	}

	public Product setPrice (long price) {
		this.price = price;
		return this;
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

	public boolean isReserved () {
		for (Reservation r : reservations)
			if (r.getStartDate().isBefore(LocalDate.now()) && r.getEndDate().isAfter(LocalDate.now()))
				return true;
		return false;
	}

	public boolean isReserved (LocalDate nStart, LocalDate nEnd) {
		for (Reservation r : reservations) {
			LocalDate rStart = r.getStartDate();
			LocalDate rEnd = r.getEndDate();
			if ((nStart.isBefore(rEnd) && nStart.isAfter(rStart))
					|| (nStart.isBefore(rEnd) && nEnd.isAfter(rEnd))
					|| (nEnd.isBefore(rEnd) && nEnd.isAfter(rStart))
					|| nStart.isEqual(rStart) || nEnd.isEqual(rStart))
				return true;
		}
		return false;
	}

	public Product addReservation (Reservation reservation) {
		reservations.add(reservation);
		return this;
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37).append(productID).append(price).append(reservations).toHashCode();
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (getClass() != o.getClass()) return false;

		Product product = (Product) o;

		return new EqualsBuilder()
				.append(productID, product.productID)
				.append(price, product.price)
				.append(reservations, product.reservations).isEquals();
	}

	@Override
	public String toString () {
		return new ToStringBuilder(this)
				.append("id", productID)
				.append("price", price)
				.append("reservation", reservations)
				.toString();
	}

}

