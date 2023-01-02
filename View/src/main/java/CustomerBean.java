import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestClient;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.*;

@Named
@ManagedBean
@ViewScoped
public class CustomerBean implements Serializable {

    @Inject
    private JwtStorage jwtStorage;
    private JSONArray customers;
    private String email;
    private JSONArray foundCustomer;
    private String chosenName;
    private boolean visible = false;
    private String prefix = "http://localhost:8081/rest/api/";
    private final RestClient restMethods;
    private final Map<Integer, Boolean> editable = new HashMap<>();
    private boolean isUpdating = false;

    public CustomerBean() {
        restMethods = new RestClient();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr;
        if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER")){
            String currentUserUsername = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
            arr = restMethods.getAll(prefix + "customers?username=" + currentUserUsername, jwtStorage.getJwt());
        } else {
            arr = restMethods.getAll(prefix + "customers", jwtStorage.getJwt());
        }
        if (arr != null) {
            this.customers = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("customerID").toString()), false);
            }
        }
    }

    public String updateCustomer(Integer id, boolean active) {
        if (active) {
            restMethods.put(prefix + "customer/" + id + "/activate", jwtStorage.getJwt());
        } else {
            restMethods.put(prefix + "customer/" + id + "/deactivate", jwtStorage.getJwt());
        }
        this.fillArray();
        return "activateCustomer";
    }

    public String update(Integer id) {
        JSONObject obj = restMethods.getOne(prefix + "customer/" + id, jwtStorage.getJwt());
        obj.put("email", this.getEmail());
        restMethods.update(obj, prefix + "customer/update", jwtStorage.getJwt());
        this.fillArray();
        this.setEmail("");
        this.isUpdating = false;
        return "submitCustomer";
    }

    public void edit(Integer id, String email) {
        if (!isUpdating) {
            this.email = email;
            this.editable.replace(id, true);
            isUpdating = true;
        }
    }

    public void findByName() {
        if (Objects.equals(this.chosenName, "") || this.chosenName == null) {
            return;
        }
        this.foundCustomer = restMethods.findByUsername(this.chosenName, this.prefix + "customers", jwtStorage.getJwt());
        this.chosenName = "";
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    public boolean isActive(int id) {
        return (Boolean) restMethods.getOne(prefix + "customer/" + id, jwtStorage.getJwt()).get("active");
    }
    public JSONArray getCustomers() {
        return customers;
    }

    public void setCustomers(JSONArray customers) {
        this.customers = customers;
    }

    public boolean getEditable(Integer id) {
        return editable.get(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JSONArray getFoundCustomer() {
        return foundCustomer;
    }

    public void setFoundCustomer(JSONArray foundCustomer) {
        this.foundCustomer = foundCustomer;
    }

    public String getChosenName() {
        return chosenName;
    }

    public void setChosenName(String chosenName) {
        this.chosenName = chosenName;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}


