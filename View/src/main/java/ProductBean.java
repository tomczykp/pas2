import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import modelBeans.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.RestClient;

import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@ManagedBean
@ViewScoped
public class ProductBean implements Serializable {

    @Inject
    private JwtStorage jwtStorage;

    private Product product;

    private RestClient restMethods;

    private JSONArray products;
    private Double updatePrice;

    private String productPrefix = "http://localhost:8081/rest/api/product";
    private String reservationPrefix = "http://localhost:8081/rest/api/reservation";
//    private JSONArray reservations;

    private final Map<Integer, Boolean> editable = new HashMap<>();
    private boolean isUpdating = false;

    public ProductBean() {
        product = new Product();
        restMethods = new RestClient();
    }

    @PostConstruct
    public void fillArray() {
        JSONArray arr = restMethods.getAll(productPrefix, jwtStorage.getJwt());
        if (arr != null) {
            this.products = arr;
            this.editable.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                editable.put(Integer.valueOf(obj.get("productID").toString()), false);
            }
        }
    }

    public String update(Integer id) {
        JSONObject obj = restMethods.getOne(productPrefix + "/" + id, jwtStorage.getJwt());
        obj.put("price", this.getUpdatePrice());
        restMethods.update(obj, productPrefix + "/update", jwtStorage.getJwt());
        this.fillArray();
        this.isUpdating = false;
        return "submitProduct";
    }

    public String createProduct() {
        restMethods.createProduct(product.getPrice(), productPrefix, jwtStorage.getJwt());
        this.fillArray();
        this.product.setPrice(0);
        return "createProduct";
    }

    public String deleteProduct(Integer id) {
        restMethods.delete(productPrefix + "/" + id, jwtStorage.getJwt());
        this.fillArray();
        this.product.setPrice(0);
        return "deleteProduct";
    }

    public JSONArray productReservations(Integer id) {
        return restMethods.getAll(reservationPrefix + "/product/" + id, jwtStorage.getJwt());
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public JSONArray getProducts() {
        return products;
    }

    public void setProducts(JSONArray products) {
        this.products = products;
    }

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
