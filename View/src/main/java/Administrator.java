import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.AdministratorBean;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestMethods;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class Administrator implements Serializable {

    private JSONArray administrators;
    private final RestMethods restMethods;
    private AdministratorBean administratorBean;

    private String adminPrefix = "http://localhost:8081/rest/api/administrator/";

    public Administrator() {
        this.restMethods = new RestMethods();
        this.administratorBean = new AdministratorBean();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(adminPrefix + "administrators");
        if (arr != null) {
            this.administrators = arr;
        }
    }

    public String createAdmin() {
        restMethods.putCustomer(administratorBean.getUsername(), administratorBean.getPassword(),  "", "ADMINISTRATOR", adminPrefix + "create/administrator");
        this.fillArray();
        return "createAdmin";
    }

    public JSONArray getAdministrators() {
        return administrators;
    }

    public void setAdministrators(JSONArray administrators) {
        this.administrators = administrators;
    }

    public AdministratorBean getAdministratorBean() {
        return administratorBean;
    }

    public void setAdministratorBean(AdministratorBean administratorBean) {
        this.administratorBean = administratorBean;
    }
}
