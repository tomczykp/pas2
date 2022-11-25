import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import modelBeans.ProductBean;
import org.json.JSONArray;
import rest.RestMethods;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class Product implements Serializable {

    private ProductBean productBean;

    private RestMethods restMethods;

    private JSONArray products;

    private String productPrefix = "http://localhost:8081/rest/api/product";

    private JSONArray reservation;

    public Product() {
        productBean = new ProductBean();
        restMethods = new RestMethods();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(productPrefix);
        if (arr != null) {
            this.products = arr;
        }
    }

    public void createProduct() {
        restMethods.createProduct(productBean.getPrice(), productPrefix);
        this.fillArray();
        this.productBean.setPrice(0);
    }

    public void deleteProduct(Integer id) {
        restMethods.delete(id, productPrefix + "/" + id);
        this.fillArray();
        this.productBean.setPrice(0);
    }

    public void productReservations(Integer id) {
        this.reservation = (JSONArray) restMethods.getOne(productPrefix + "/" + id).get("reservations");
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

    public JSONArray getReservation() {
        return reservation;
    }

    public void setReservation(JSONArray reservation) {
        this.reservation = reservation;
    }
}
