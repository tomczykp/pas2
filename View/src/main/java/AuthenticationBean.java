import jakarta.annotation.ManagedBean;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import rest.RestClient;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
@Named
@ManagedBean
@ViewScoped
public class AuthenticationBean implements Serializable {

    private String username;

    private String password;

    @Inject
    private JwtStorage jwtStorage;

    @Inject
    private HttpServletRequest request;
    private final RestClient restClient;

    public AuthenticationBean() {
        restClient = new RestClient();
    }

    public String login() {
        try {
            String jwt = this.restClient.login(this.username, this.password, "http://localhost:8081/rest/api/login");
            try {
                request.logout();
            } catch (ServletException ignored) {}
            this.jwtStorage.setJwt(jwt);
            return "logged";
        } catch (Exception e) {
            if (e.getMessage().equals("Wrong credentials!")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
                return "exception";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return "exception";
        }
    }

    public String logout() {
        try {
            request.logout();
        } catch (ServletException e) {}
        this.jwtStorage.setJwt("");
        return "logout";
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
