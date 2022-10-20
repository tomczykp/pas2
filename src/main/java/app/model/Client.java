package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client")
@Table(name = "client")
@Access(AccessType.FIELD)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;

    @Version
    private Integer version;

    @Column
    private double income;

    @Column(unique = true)
    @NotNull
    private String phoneNumber;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride( name = "country", column = @Column(name = "addressCountry")),
                    @AttributeOverride( name = "city", column = @Column(name = "addressCity")),
                    @AttributeOverride( name = "street", column = @Column(name = "addressStreet")),
                    @AttributeOverride( name = "number", column = @Column(name = "addressNumber"))
            }
    )
    private Address adress;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    protected Set<Account> accounts = new HashSet<>();

    public Client() {}

    public Client(double income, String phoneNumber, Address address) {
        this.income = income;
        this.phoneNumber = phoneNumber;
        this.adress = address;
    }

    public int getId() {
        return clientId;
    }

    public double getIncome() {
        return income;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAdress() {
        return adress;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAdress(Address adress) {
        this.adress = adress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", clientId)
                .append("income", income)
                .append("phoneNumber", phoneNumber)
                .append("adress", adress)
                .toString();
    }

   public String getNip() {
        return "Nip";
    }

    public String getSurname() {
        return "Surname";
    }

    public String getPersonalNumber() {
        return "Personal Number";
    }

    public String getName() {
        return "Name";
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;

        if (!(o instanceof Client client)) return false;

        return new EqualsBuilder().append(clientId, client.clientId).append(getIncome(), client.getIncome()).append(version, client.version).append(getPhoneNumber(), client.getPhoneNumber()).append(getAdress(), client.getAdress()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(clientId).toHashCode();
    }
}
