import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestMethods;

import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@ManagedBean
@ViewScoped
public class Reservation implements Serializable {

    private JSONArray reservations;
    private String reservationPrefix = "http://localhost:8081/rest/api/reservation";
    private final RestMethods rest;

    private Integer updateCustomerID = 0;
    private Integer updateProductID = 0;
    private String updateStartDate = "";
    private String updateEndDate = "";
    private boolean isUpdating = false;
    private final Map<Integer, Boolean> editable = new HashMap<>();

    public Reservation() {
        rest = new RestMethods();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = rest.getAll(reservationPrefix);
        if (arr != null) {
            this.reservations = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("reservationID").toString()), false);
            }
        }
    }

    public void deleteF(Integer id) {
        rest.delete(reservationPrefix + "/forced/" + id);
        this.fillArray();
    }

    public void update(Integer id) {
        JSONObject obj = rest.getOne(reservationPrefix + "/" + id);
        obj.put("customer", this.getUpdateCustomerID());
        obj.put("product", this.getUpdateProductID());
        obj.put("startDate", this.getUpdateStartDate());
        obj.put("endDate", this.getUpdateEndDate());
        rest.update(obj, reservationPrefix + "/update");
        this.fillArray();
        this.setUpdateCustomerID(0);
        this.setUpdateProductID(0);
        this.setUpdateStartDate("");
        this.setUpdateEndDate("");
        this.isUpdating = false;
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
