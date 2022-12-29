import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@Named
@ManagedBean
@ApplicationScoped
public class UserRole implements Serializable{

    public boolean isAdministrator() {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole("ADMINISTRATOR");
    }

    public boolean isCustomer() {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER");
    }

    public boolean isModerator() {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole("MODERATOR");
    }

    public boolean isAnonymous() {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole("ANONYMOUS");
    }

    public String getRole() {
        if (this.isAdministrator()) {
            return "ADMINISTRATOR";
        }
        if (this.isModerator()) {
            return "MODERATOR";
        }
        if (this.isCustomer()) {
            return "CUSTOMER";
        }
        if (this.isAnonymous()) {
            return "ANONYMOUS";
        }
        return "UNKNOWN";
    }
}
