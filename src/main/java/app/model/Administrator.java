package app.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement
public class Administrator {

    @XmlElement
    private int AdministratorID;

    @XmlElement
    private String username;

    @XmlElement
    private String password;

    @XmlElement
    private CustomerType type;

    public Administrator(String u, String p) {
           username = u;
           password = p;
           type = CustomerType.ADMINISTRATOR;
       }


    public Administrator() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Administrator that = (Administrator) o;

        return new EqualsBuilder().append(AdministratorID, that.AdministratorID).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(AdministratorID).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("AdministratorID", AdministratorID)
                .append(username)
                .toString();
    }

    public int getAdministratorID() {
        return AdministratorID;
    }

    public void setAdministratorID(int administratorID) {
        AdministratorID = administratorID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }


}
