package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Person")
public class Person extends Client {

    @Column
    private String name;

    @Column
    private String surname;

    @NotNull
    @Column
    private String personalNumber;

    public Person(double income, String phoneNumber, Address address, String name, String surname, String personalNumber) {
        super(income, phoneNumber, address);
        this.name = name;
        this.surname = surname;
        this.personalNumber = personalNumber;
    }

    public Person() {

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("surname", surname)
                .append("personalNumber", personalNumber)
                .toString();
    }
}
