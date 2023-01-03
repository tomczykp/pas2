import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestClient;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.IOException;
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

    private String username;
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

    public String activate(Integer id, boolean active) {
        if (active) {
            restMethods.put(prefix + "customer/" + id + "/activate", jwtStorage.getJwt());
        } else {
            restMethods.put(prefix + "customer/" + id + "/deactivate", jwtStorage.getJwt());
        }
        this.fillArray();
        return "activateCustomer";
    }

    public String update(Integer id) throws IOException {
        HttpResponse response = restMethods.getOne(prefix + "customer/" + id, jwtStorage.getJwt());
        String jws = response.getFirstHeader("ETag").getValue();
        JSONObject obj = new JSONObject(new BasicResponseHandler().handleResponse(response));
        obj.put("email", this.getEmail());
        obj.put("username", this.getUsername());
        try {
            restMethods.update(obj, prefix + "customer/update", jwtStorage.getJwt(), jws);
        } catch (Exception e) {
            if (e.getMessage().contains("JWS")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
                return "";
            }
        }
        this.fillArray();
        this.setEmail("");
        this.setUsername("");
        this.isUpdating = false;
        return "submitCustomer";
    }

    public void edit(Integer id, String email, String username) {
        if (!isUpdating) {
            this.email = email;
            this.username = username;
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

    public boolean isActive(int id) throws IOException {
        HttpResponse response = restMethods.getOne(prefix + "customer/" + id, jwtStorage.getJwt());
        JSONObject obj = new JSONObject(new BasicResponseHandler().handleResponse(response));
        return (Boolean) obj.get("active");
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


