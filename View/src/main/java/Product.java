import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.ProductBean;
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
public class Product implements Serializable {

    private ProductBean productBean;

    private RestMethods restMethods;

    private JSONArray products;
    private Double updatePrice;

    private String productPrefix = "http://localhost:8081/rest/api/product";
    private String reservationPrefix = "http://localhost:8081/rest/api/reservation";
//    private JSONArray reservations;

    private Map<Integer, Boolean> editable = new HashMap<>();
    private boolean isUpdating = false;

    public Product() {
        productBean = new ProductBean();
        restMethods = new RestMethods();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(productPrefix);
        if (arr != null) {
            this.products = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("productID").toString()), false);
            }
        }
    }

    public void update(Integer id) {
        JSONObject obj = restMethods.getOne(productPrefix + "/" + id);
        if (this.updatePrice <= 0 || this.updatePrice == null) {
            this.editable.replace(id, false);
            this.isUpdating = false;
            return;
        }
        obj.put("price", this.getUpdatePrice());
        restMethods.update(obj, productPrefix + "/update");
        this.fillArray();
        this.isUpdating = false;
    }

    public void createProduct() {
        restMethods.createProduct(productBean.getPrice(), productPrefix);
        this.fillArray();
        this.productBean.setPrice(0);
    }

    public void deleteProduct(Integer id) {
        restMethods.delete(productPrefix + "/" + id);
        this.fillArray();
        this.productBean.setPrice(0);
    }

    public JSONArray productReservations(Integer id) {
        return restMethods.getAll(reservationPrefix + "/product/" + id);
    }

    public ProductBean getProductBean() {
        return productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }

    public JSONArray getProducts() {
        return products;
    }

    public void setProducts(JSONArray products) {
        this.products = products;
    }

//    public JSONArray getReservations() {
//        return reservations;
//    }
//
//    public void setReservations(JSONArray reservations) {
//        this.reservations = reservations;
//    }

    public void edit(Integer id, Double price) {
        if (!isUpdating) {
            this.updatePrice = price;
            this.editable.replace(id, true);
            isUpdating = true;
        }
    }

    public Double getUpdatePrice() {
        return updatePrice;
    }

    public void setUpdatePrice(Double updatePrice) {
        this.updatePrice = updatePrice;
    }

    public boolean getEditable(Integer id) {
        return editable.get(id);
    }
}
