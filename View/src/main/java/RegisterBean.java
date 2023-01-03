import jakarta.annotation.ManagedBean;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Scope;
import modelBeans.Customer;
import rest.RestClient;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class RegisterBean implements Serializable {

    @Inject
    private JwtStorage jwtStorage;

    private Customer customer;

    private RestClient restMethods;

    public RegisterBean() {
        restMethods = new RestClient();
        customer = new Customer();
    }

    public String createCustomer() {
        try {
            restMethods.putCustomer(customer.getUsername(), customer.getPassword(), customer.getEmail(), "CUSTOMER", "http://localhost:8081/rest/api/" + "customer/create", jwtStorage.getJwt());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return "exception";
        }
        return "createCustomer";
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
