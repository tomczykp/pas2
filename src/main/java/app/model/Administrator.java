package app.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement
public class Administrator extends User {

    public Administrator(String u, String p) {
           super(u, p, CustomerType.ADMINISTRATOR);
       }


    public Administrator() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Administrator that = (Administrator) o;

        return new EqualsBuilder().append(super.getUserID(), that.getUserID()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(super.getUserID()).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("AdministratorID", super.getUserID())
                .append("Username", super.getUsername())
                .toString();
    }
}
