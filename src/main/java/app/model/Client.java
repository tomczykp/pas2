package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client {
	@Id
	@Column(name = "clientId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int id;

	@NotNull
	@Column(unique = true)
	private String login;

	@NotNull
	@Column
	private String name;

	@NotNull
	@Column
	private String surname;

	@NotNull
	@Column
	private boolean active;

	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
	protected Set<Reservation> reservations = new HashSet<>();


	public Client () {}

	public Client (String n, String s) {
		this.name = n;
		this.surname = s;
		this.active = true;
		this.login = n.charAt(0) + s.substring(1);
	}

	public String getName () {
		return name;
	}

	public String getSurname () {
		return surname;
	}

	public int get () {
		return id;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (o.getClass() != this.getClass()) return false;
		Client client = (Client)o;

		return new EqualsBuilder().append(get(), client.get()).append(getName(), client.getName()).append(getSurname(), client.getSurname()).isEquals();
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37).append(get()).append(getName()).append(getSurname()).toHashCode();
	}

	@Override
	public String toString () {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("name", name)
				.append("surname", surname)
				.toString();
	}

}


