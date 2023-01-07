package app.dto;

import app.model.Administrator;
import app.model.CustomerType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdministratorDTO {

    @XmlElement
    public int administratorID;
    @XmlElement
    public String username;
    @XmlElement
    public CustomerType type;

    public AdministratorDTO(Administrator a) {
        username = a.getUsername();
        administratorID = a.getUserID();
        type = a.getType();
    }

    public AdministratorDTO() {}
}
