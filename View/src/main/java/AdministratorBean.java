import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelBeans.Administrator;
import org.json.JSONArray;
import rest.RestClient;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class AdministratorBean implements Serializable {

    @Inject
    private JwtStorage jwtStorage;

    private JSONArray administrators;
    private final RestClient restMethods;
    private Administrator administrator;

    private String prefix = "http://localhost:8081/rest/api/";

    public AdministratorBean() {
        this.restMethods = new RestClient();
        this.administrator = new Administrator();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(prefix + "administrators", jwtStorage.getJwt());
        if (arr != null) {
            this.administrators = arr;
        }
    }

    public String createAdmin() {
        try {
            restMethods.putCustomer(administrator.getUsername(), administrator.getPassword(), "", "ADMINISTRATOR", prefix + "administrator/create", jwtStorage.getJwt());
        } catch (Exception e) {

        }
        this.fillArray();
        return "createAdmin";
    }

    public JSONArray getAdministrators() {
        return administrators;
    }

    public void setAdministrators(JSONArray administrators) {
        this.administrators = administrators;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }
}
