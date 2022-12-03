import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.CustomerBean;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestMethods;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.*;

@Named
@ManagedBean
@ViewScoped
public class Customer implements Serializable {

    private JSONArray customers;
    private String email;
    private JSONArray foundCustomer;
    private String chosenName;
    private boolean visible = false;
    private String adminPrefix = "http://localhost:8081/rest/api/administrator/";
    private final RestMethods restMethods;
    private CustomerBean beanCustomer;
    private final Map<Integer, Boolean> editable = new HashMap<>();
    private boolean isUpdating = false;

    public Customer() {
        restMethods = new RestMethods();
        beanCustomer = new CustomerBean();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(adminPrefix + "customers");
        if (arr != null) {
            this.customers = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("customerID").toString()), false);
            }
        }
    }

    public String createCustomer() {
        restMethods.putCustomer(beanCustomer.getUsername(), beanCustomer.getPassword(), beanCustomer.getEmail(), "CUSTOMER",  adminPrefix + "create/customer");
        this.fillArray();
        return "createCustomer";
    }

    public String updateCustomer(Integer id, boolean active) {
        if (active) {
            restMethods.put(adminPrefix + id + "/activate");
        } else {
            restMethods.put(adminPrefix + id + "/deactivate");
        }
        this.fillArray();
        return "activateCustomer";
    }

    public String update(Integer id) {
        JSONObject obj = restMethods.getOne(adminPrefix + "customer/" + id);
        obj.put("email", this.getEmail());
        restMethods.update(obj, adminPrefix + "update/customer");
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
        this.foundCustomer = restMethods.findByUsername(this.chosenName, this.adminPrefix + "customers");
        this.chosenName = "";
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }



    public boolean isActive(int id) {
        return (Boolean) restMethods.getOne(adminPrefix + "customer/" + id).get("active");
    }
    public JSONArray getCustomers() {
        return customers;
    }

    public void setCustomers(JSONArray customers) {
        this.customers = customers;
    }

    public CustomerBean getBeanCustomer() {
        return beanCustomer;
    }

    public void setBeanCustomer(CustomerBean beanCustomer) {
        this.beanCustomer = beanCustomer;
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


