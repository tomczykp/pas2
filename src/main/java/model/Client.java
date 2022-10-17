package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "client")
public class Client {
	@Id
	@Column(name = "clientId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int id;

	@NotNull
	@Column
	private String name;

	@NotNull
	@Column
	private String surname;

	public Client () {}

	public Client (String n, String s) {
		this.name = n;
		this.surname = s;
	}

	public String getName () {
		return name;
	}

	public String getSurname () {
		return surname;
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (!(o instanceof Client client)) return false;

		return new EqualsBuilder().append(getId(), client.getId()).append(getName(), client.getName()).append(getSurname(), client.getSurname()).isEquals();
	}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37).append(getId()).append(getName()).append(getSurname()).toHashCode();
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


