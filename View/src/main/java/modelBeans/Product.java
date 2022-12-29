package modelBeans;

import org.json.JSONArray;

public class Product {

    private  int productID;
    private double price;
    private JSONArray reservations;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public JSONArray getReservations() {
        return reservations;
    }

    public void setReservations(JSONArray reservations) {
        this.reservations = reservations;
    }
}
