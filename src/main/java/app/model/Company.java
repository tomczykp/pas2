package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Company")
public class Company extends Client {
    @Column
    private String name;

    @Column(unique = true)
    @NotNull
    private String NIP;

    public Company() {}

    public Company(double income, String phoneNumber, Address address, String name, String NIP) {
        super(income, phoneNumber, address);
        this.name = name;
        this.NIP = NIP;
    }

    public String getName() {
        return name;
    }

    public String getNIP() {
        return NIP;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("NIP", NIP)
                .toString();
    }
}
