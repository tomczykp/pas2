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
import java.util.HashMap;
import java.util.Map;

@Named
@ManagedBean
@ViewScoped
public class ReservationBean implements Serializable {

    @Inject
    private JwtStorage jwtStorage;

    private JSONArray reservations;
    private String reservationPrefix = "http://localhost:8081/rest/api/reservation";
    private final RestClient rest;

    private Integer updateCustomerID = 0;
    private Integer updateProductID = 0;
    private String updateStartDate = "";
    private String updateEndDate = "";
    private boolean isUpdating = false;
    private final Map<Integer, Boolean> editable = new HashMap<>();

    public ReservationBean() {
        rest = new RestClient();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = rest.getAll(reservationPrefix, jwtStorage.getJwt());
        if (arr != null) {
            this.reservations = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("reservationID").toString()), false);
            }
        }
    }

    public String deleteF(Integer id) {
            rest.delete(reservationPrefix + "/forced/" + id, jwtStorage.getJwt());
            this.fillArray();
            return "deleteReservation";
    }

    public String update(Integer id) throws IOException {
        HttpResponse response = rest.getOne(reservationPrefix + "/" + id, jwtStorage.getJwt());
        String jws = response.getFirstHeader("ETag").getValue();
        JSONObject obj = new JSONObject(new BasicResponseHandler().handleResponse(response));
        obj.put("customer", this.getUpdateCustomerID());
        obj.put("product", this.getUpdateProductID());
        obj.put("startDate", this.getUpdateStartDate());
        obj.put("endDate", this.getUpdateEndDate());
        try {
            rest.update(obj, reservationPrefix + "/update", jwtStorage.getJwt(), jws);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
        this.fillArray();
        this.setUpdateCustomerID(0);
        this.setUpdateProductID(0);
        this.setUpdateStartDate("");
        this.setUpdateEndDate("");
        this.isUpdating = false;
        return "submitReservation";
    }

    public JSONArray getReservations() {
        return reservations;
    }

    public void setReservations(JSONArray reservations) {
        this.reservations = reservations;
    }

    public Integer getUpdateCustomerID() {
        return updateCustomerID;
    }

    public void setUpdateCustomerID(Integer updateCustomerID) {
        this.updateCustomerID = updateCustomerID;
    }

    public Integer getUpdateProductID() {
        return updateProductID;
    }

    public void setUpdateProductID(Integer updateProductID) {
        this.updateProductID = updateProductID;
    }

    public String getUpdateStartDate() {
        return updateStartDate;
    }

    public void setUpdateStartDate(String updateStartDate) {
        this.updateStartDate = updateStartDate;
    }

    public String getUpdateEndDate() {
        return updateEndDate;
    }

    public void setUpdateEndDate(String updateEndDate) {
        this.updateEndDate = updateEndDate;
    }

    public void edit(Integer id, Integer customerID, Integer productID, String sDate, String eDate) {
        if (!isUpdating) {
            this.updateCustomerID = customerID;
            this.updateProductID = productID;
            this.updateStartDate = sDate;
            this.updateEndDate = eDate;
            this.editable.replace(id, true);
            this.isUpdating = true;
        }
    }

    public boolean getEditable(Integer id) {
        return editable.get(id);
    }

}
