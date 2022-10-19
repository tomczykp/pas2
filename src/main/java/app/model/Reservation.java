package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservation")
public class Reservation {

	@Id
	@Column(name = "resId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int id;

	@NotNull
	@Column
	private LocalDateTime startDate;

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (o.getClass() != this.getClass()) return false;
		Reservation that = (Reservation) o;

		return new EqualsBuilder().append(id, that.id).append(startDate, that.startDate).append(endDate, that.endDate).append(client, that.client).append(product, that.product).isEquals();
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37).append(id).append(startDate).append(endDate).append(client).append(product).toHashCode();
	}

	@NotNull
	@Column
	private LocalDateTime endDate;

	@NotNull
	@ManyToOne
	private Client client;

	@OneToMany
	@NotNull
	private List<Product> product = new ArrayList<>();

	public Reservation(LocalDateTime endDate, Client client, List<Product> product) {
		this.startDate = LocalDateTime.now();
		this.endDate = endDate;
		this.client = client;
		this.product = product;
	}

	public Reservation() {

	}
}
