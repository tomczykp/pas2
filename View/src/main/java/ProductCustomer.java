import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
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
    private JSONArray products;
    private JSONArray reservations;
    private final RestMethods restMethods;
    private ReservationBean reservationBean;
    private String reservationPrefix = "http://localhost:8081/rest/api/reservation";
    private String productPrefix = "http://localhost:8081/rest/api/product";

    @Inject
    private ChosenID chosenID;

    public ProductCustomer() {
        restMethods = new RestMethods();
        reservationBean = new ReservationBean();
    }

    @PostConstruct
    public void innit() {
        fillArray();
        fillReservationArray(chosenID.getId());
    }

    public void fillArray() {
        JSONArray arr = restMethods.getAll(productPrefix);
        if (arr != null) {
            this.products = arr;
        }
    }

    public void createReservation(Integer clientId, Integer productId) {
        restMethods.createReservation(reservationBean.getStartDate(), reservationBean.getEndDate(), clientId, productId, reservationPrefix);
        this.fillArray();
        this.fillReservationArray(clientId);
    }

    public void fillReservationArray(Integer clientId) {
        this.reservations = restMethods.getAll(reservationPrefix + "/client/" + clientId);
    }

    public void delete(Integer id, Integer clientId) {
        restMethods.delete(reservationPrefix + "/" + id);
        fillReservationArray(clientId);
    }

    public JSONArray getProducts() {
        return products;
    }

    public void setProducts(JSONArray products) {
        this.products = products;
    }

    public ReservationBean getReservationBean() {
        return reservationBean;
    }

    public void setReservationBean(ReservationBean reservationBean) {
        this.reservationBean = reservationBean;
    }

    public JSONArray getReservations() {
        return reservations;
    }

    public void setReservations(JSONArray reservations) {
        this.reservations = reservations;
    }
}
