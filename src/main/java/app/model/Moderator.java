package app.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement
public class Moderator {

    @XmlElement
    private int moderatorID;

    @XmlElement
    private String email;

    @XmlElement
    private CustomerType type;

    @XmlElement
    private String username;

    @XmlElement
    private String password;

    public Moderator(String u, String e, String p) {
        type = CustomerType.MODERATOR;
        username = u;
        password = p;
        email = e;
    }

    public Moderator() {}

    public int getModeratorID() {
        return moderatorID;
    }

    public void setModeratorID(int moderatorID) {
        this.moderatorID = moderatorID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Moderator moderator = (Moderator) o;

        return new EqualsBuilder().append(moderatorID, moderator.moderatorID).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(moderatorID).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("moderatorID", moderatorID)
                .append("email", email)
                .append(username)
                .append("type", type)
                .toString();
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
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
}
