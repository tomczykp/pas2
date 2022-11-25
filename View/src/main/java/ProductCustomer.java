import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.ProductBean;
import modelBeans.ReservationBean;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestMethods;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class ProductCustomer implements Serializable {
    private Integer customerID;
    private JSONArray products;
    private JSONObject customer;
    private final RestMethods restMethods;
    private ProductBean productBean;
    private ReservationBean reservationBean;
    private String reservationPrefix = "http://localhost:8081/rest/api/reservation";
    private String productPrefix = "http://localhost:8081/rest/api/product";
    private String adminPrefix = "http://localhost:8081/rest/api/administrator/";

    public ProductCustomer() {
        restMethods = new RestMethods();
        productBean = new ProductBean();
        reservationBean = new ReservationBean();
    }


    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(productPrefix);
        if (arr != null) {
            this.products = arr;
        }
    }

    public void createReservation(Integer clientId, Integer productId) {
        JSONObject customer = restMethods.getOne(adminPrefix + "customer/" + clientId);
        JSONObject product = restMethods.getOne(productPrefix + "/" + productId);
        restMethods.createReservation(reservationBean.getStartDate(), reservationBean.getEndDate(), customer, product, reservationPrefix);
        this.fillArray();
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public JSONArray getProducts() {
        return products;
    }

    public void setProducts(JSONArray products) {
        this.products = products;
    }

    public JSONObject getCustomer() {
        return customer;
    }

    public void setCustomer(JSONObject customer) {
        this.customer = customer;
    }

    public ProductBean getProductBean() {
        return productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }

    public ReservationBean getReservationBean() {
        return reservationBean;
    }

    public void setReservationBean(ReservationBean reservationBean) {
        this.reservationBean = reservationBean;
    }
}
