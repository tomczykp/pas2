import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Named
@ManagedBean
@SessionScoped
public class UserRole implements Serializable {

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
}
