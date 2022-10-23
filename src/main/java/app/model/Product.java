package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@Column(name = "productId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int id;

	@NotNull
	@Column
	private long price;

	@OneToMany
	private final List<Reservation> reservations  = new ArrayList<>();

	public Product(long price) {
		this.price = price;
	}

	public Product() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public List<Reservation> getReservations() {
		return this.reservations;
	}

	public void addReservation(Reservation reservation) {
		this.reservations.add(reservation);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (this.getClass() != o.getClass()) return false;

		Product product = (Product) o;

		return new EqualsBuilder().append(getId(), product.getId()).append(getPrice(), product.getPrice()).append(getReservations(), product.getReservations()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).append(getPrice()).append(getReservations()).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("price", price)
				.append("reservation", reservations)
				.toString();
	}
}

