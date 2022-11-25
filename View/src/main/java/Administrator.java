import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.CustomerBean;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestMethods;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class Administrator implements Serializable {

    private JSONArray customers;
    private String adminPrefix = "http://localhost:8081/rest/api/administrator/";
    private final RestMethods restMethods;

    private CustomerBean beanCustomer;
    private Integer currentID;

    public Administrator() {
        restMethods = new RestMethods();
        beanCustomer = new CustomerBean();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(adminPrefix + "customers");
        if (arr != null) {
            this.customers = arr;
        }
    }

    public void createCustomer() {
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
        restMethods.updateCustomer(obj,  adminPrefix + "update/customer");
        this.fillArray();
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

    public Integer getCurrentID() {
        return currentID;
    }

    public void setCurrentID(Integer currentID) {
        this.currentID = currentID;
    }
}


