import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelBeans.Reservation;
import org.json.JSONArray;
import rest.RestClient;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class ProductCustomerBean implements Serializable {

    @Inject
    private JwtStorage jwtStorage;
    private JSONArray products;
    private JSONArray reservations;
    private final RestClient restMethods;
    private Reservation reservation;
    private String reservationPrefix = "http://localhost:8081/rest/api/reservation";
    private String productPrefix = "http://localhost:8081/rest/api/product";

    @Inject
    private ChosenIdBean chosenID;

    public ProductCustomerBean() {
        restMethods = new RestClient();
        reservation = new Reservation();
    }

    @PostConstruct
    public void innit() {
        fillArray();
        fillReservationArray(chosenID.getId());
    }

    public void fillArray() {
        JSONArray arr = restMethods.getAll(productPrefix, jwtStorage.getJwt());
        if (arr != null) {
            this.products = arr;
        }
    }

    public String createReservation(Integer clientId, Integer productId) {
        restMethods.createReservation(reservation.getStartDate(), reservation.getEndDate(), clientId, productId, reservationPrefix, jwtStorage.getJwt());
        this.fillArray();
        this.fillReservationArray(clientId);
        return "book";
    }

    public void fillReservationArray(Integer clientId) {
        this.reservations = restMethods.getAll(reservationPrefix + "/client/" + clientId, jwtStorage.getJwt());
    }

    public String delete(Integer id, Integer clientId) {
        restMethods.delete(reservationPrefix + "/" + id, jwtStorage.getJwt());
        fillReservationArray(clientId);
        return "deletePC";
    }

    public JSONArray getProducts() {
        return products;
    }

    public void setProducts(JSONArray products) {
        this.products = products;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public JSONArray getReservations() {
        return reservations;
    }

    public void setReservations(JSONArray reservations) {
        this.reservations = reservations;
    }
}
