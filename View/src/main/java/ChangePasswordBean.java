import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import rest.RestClient;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@Named
@ManagedBean
@RequestScoped
public class ChangePasswordBean implements Serializable {

    private RestClient restClient;

    @Inject
    private JwtStorage jwtStorage;
    private String oldPassword;
    private String newPassword;
    private String newPasswordRepeat;

    public String changePassword() {
        this.restClient = new RestClient();
        if (!this.newPassword.equals(this.newPasswordRepeat)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Given new passwords are diferent!"));
            return "wrongPasswords";
        }
        try {
            this.restClient.changePassword(this.oldPassword, this.newPassword, "http://localhost:8081/rest/api/passwordChange", this.jwtStorage.getJwt());
            return "changed";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return "wrongPasswords";
        }
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRepeat() {
        return newPasswordRepeat;
    }

    public void setNewPasswordRepeat(String newPasswordRepeat) {
        this.newPasswordRepeat = newPasswordRepeat;
    }
}
