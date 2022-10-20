package app.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
@Access(AccessType.FIELD)
public class Address {

    private String country;
    private String city;
    private String street;
    private String number;

    public Address() {

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("country", country)
                .append("city", city)
                .append("street", street)
                .append("number", number)
                .toString();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Address(String country, String city, String street, String number) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
    }

    @Override
    public int hashCode () {
        return new HashCodeBuilder(17, 37).append(getCountry()).append(getCity()).append(getStreet()).append(getNumber()).toHashCode();
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;

        if (!(o instanceof Address address)) return false;

        return new EqualsBuilder()
                .append(getCountry(), address.getCountry())
                .append(getCity(), address.getCity())
                .append(getStreet(), address.getStreet())
                .append(getNumber(), address.getNumber())
                .isEquals();
    }


}
