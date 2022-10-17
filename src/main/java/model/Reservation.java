package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Reservation {

	@Id
	@Column(name = "resId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int id;

	@NotNull
	@Column
	private LocalDateTime startDate;

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
