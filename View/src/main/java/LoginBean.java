import jakarta.annotation.ManagedBean;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.json.JSONObject;
import rest.RestClient;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

    private String username;

    private String password;

    @Inject
    private JwtStorage jwtStorage;
    private final RestClient restClient;

    public LoginBean() {
        restClient = new RestClient();
    }

    public void login() {
        JSONObject loginResponse = this.restClient.login(this.username, this.password, "http://localhost:8081/rest/api/login");
        String jwt = loginResponse.get("jwt").toString();
        this.jwtStorage.setJwt(jwt);
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
