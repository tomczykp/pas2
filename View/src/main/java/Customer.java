import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.CustomerBean;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestMethods;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.*;

@Named
@ManagedBean
@ViewScoped
public class Customer implements Serializable {

    private JSONArray customers;
    private String email;
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

    public void createCustomer() {
        if (beanCustomer.getPassword() == null) {
            return;
        }
        restMethods.putCustomer(beanCustomer.getUsername(), beanCustomer.getPassword(), beanCustomer.getEmail(), "CUSTOMER",  adminPrefix + "create/customer");
        this.fillArray();
    }

    public void updateCustomer(Integer id, boolean active) {
        JSONObject obj = restMethods.getOne(adminPrefix + "customer/" + id);
        if (active) {
            if (obj.get("active").toString().equals("true")) {
                return;
            }
            obj.put("active", true);
        } else {
            if (obj.get("active").toString().equals("false")) {
                return;
            }
            obj.put("active", false);
        }
        restMethods.update(obj,  adminPrefix + "update/customer");
        this.fillArray();
    }

    public void update(Integer id) {
        JSONObject obj = restMethods.getOne(adminPrefix + "customer/" + id);
        if (this.getEmail().isEmpty()) {
            this.editable.replace(id, false);
            this.isUpdating = false;
            return;
        }
        obj.put("email", this.getEmail());
        restMethods.update(obj, adminPrefix + "update/customer");
        this.fillArray();
        this.setEmail("");
        this.isUpdating = false;
    }

    public void edit(Integer id) {
        if (!isUpdating) {
            this.editable.replace(id, true);
            isUpdating = true;
        }
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
}


