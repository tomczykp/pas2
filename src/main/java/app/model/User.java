package app.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlTransient;
@XmlTransient
@XmlSeeAlso({Administrator.class, Moderator.class, Customer.class})
public abstract class User {
    @XmlElement
    private int userID;
    @XmlElement
    private String username;
    @XmlElement
    private String password;
    @XmlElement
    private CustomerType type;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public User() {
    }

    public User(int userID, String username, String password, CustomerType type) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public User(String username, String password, CustomerType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }
}

