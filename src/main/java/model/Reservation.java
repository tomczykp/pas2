package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "client")
public class Reservation {


	@Column(name = "resId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int id;

	@NotNull
	@Column
	private Date startDate;

	@NotNull
	@Column
	private Date endDate;

	@NotNull
	@ManyToOne
	private Client client;


}
