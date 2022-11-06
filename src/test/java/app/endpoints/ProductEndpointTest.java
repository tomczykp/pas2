package app.endpoints;

import app.model.Product;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductEndpointTest {

	private RequestSpecification req() {
		return RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/rest/api")
				.port(8080);
	}

	List<JsonPath> ids;
	@BeforeAll
	public void init() {
		ids = new ArrayList<>();
		ids.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0)).extract().response().jsonPath());

		ids.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 300)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(300))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0)).extract().response().jsonPath());
	}

	@Test
	public void getTest() {

		req()
				.get("/product").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.anything());

		req()
				.get("/product/-4").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));

		req()
				.get("/product/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

	}

	@Test
	public void putTest() {

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.put("/product").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing parameter 'price'"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", "20a")
				.put("/product").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0));
	}

	@Test
	public void deleteTest() {

		Map<Integer, LinkedHashMap> m = req().when().get("/product").getBody().jsonPath().getMap("", Integer.class, LinkedHashMap.class);
		Assertions.assertFalse(m.isEmpty());

		int price = ids.get(0).get("price");
		int productID = ids.get(0).get("productID");

		req().get("/product/" + productID).then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.equalTo(productID))
				.body("price", Matchers.equalTo(price))
				.body("reservations", Matchers.hasSize(0));

		req()
				.delete("/product/" + productID +"a").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req().get("/product/" + productID).then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.equalTo(productID))
				.body("price", Matchers.equalTo(price))
				.body("reservations", Matchers.hasSize(0));

		req()
				.delete("/product/" + productID).then()
				.body("status", Matchers.equalTo("deletion succesful"));

		req()
				.get("/product/" + productID).then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));
	}


}
