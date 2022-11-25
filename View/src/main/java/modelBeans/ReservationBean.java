package modelBeans;

import org.json.JSONObject;

public class ReservationBean {

    private int reservationID;
    private String startDate;
    private String endDate;
    private JSONObject customer;
    private JSONObject product;

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public JSONObject getCustomer() {
        return customer;
    }

    public void setCustomer(JSONObject customer) {
        this.customer = customer;
    }

    public JSONObject getProduct() {
        return product;
    }

    public void setProduct(JSONObject product) {
        this.product = product;
    }
}
