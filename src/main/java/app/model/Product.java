package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@Column(name = "productId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int productID;
	@NotNull
	@Column
	private long price;

	@OneToMany
	private final List<Reservation> reservations = new ArrayList<>();

	public Product (long price) {
		this.price = price;
	}

	public Product () {}

	public int getProductID () {
		return productID;
	}

	public void setId (int id) {
		productID = id;
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


	public List<Reservation> getCurrentReservations () {
		List<Reservation> reservationList = new ArrayList<>();
		for (Reservation r : reservations)
			if (r.getEndDate().isAfter(LocalDateTime.now()))
				reservationList.add(r);
		return reservationList;
	}
	public List<Reservation> getPastReservations () {
		List<Reservation> reservationList = new ArrayList<>();
		for (Reservation r : reservations)
			if (r.getEndDate().isBefore(LocalDateTime.now()))
				reservationList.add(r);
		return reservationList;
	}

	public void addReservation (Reservation reservation) {
		reservations.add(reservation);
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

