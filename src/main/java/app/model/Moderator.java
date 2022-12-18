package app.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement
public class Moderator extends User {

    @XmlElement
    private String email;

    public Moderator(String u, String e, String p) {
        super(u, p, CustomerType.MODERATOR);
        this.email = e;
    }

    public Moderator() {}

    public String getEmail() {
        return email;
    }

    public Moderator setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Moderator moderator = (Moderator) o;

        return new EqualsBuilder().append(super.getUserID(), moderator.getUserID()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(super.getUserID()).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("moderatorID", super.getUserID())
                .append("email", email)
                .append("username", super.getUsername())
                .append("type", super.getType())
                .toString();
    }
}
