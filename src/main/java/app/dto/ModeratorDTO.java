package app.dto;

import app.model.CustomerType;
import app.model.Moderator;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModeratorDTO {

    @XmlElement
    public int moderatorID;
    @XmlElement
    public String email;
    @XmlElement
    public String username;
    @XmlElement
    public CustomerType type;

    public ModeratorDTO(Moderator m) {
        email = m.getEmail();
        username = m.getUsername();
        moderatorID = m.getUserID();
        type = m.getType();
    }

    public ModeratorDTO() {}
}
