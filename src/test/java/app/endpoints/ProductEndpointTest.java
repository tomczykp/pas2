package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ProductEndpointTest {

	private RequestSpecification req() {
		return RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/rest/api")
				.port(8080);
	}

	@Test
	public void getTest() {
		req()
				.delete("/product").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.get("/product").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.is("{}"));

		req()
				.get("/product/1").then()
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
				.delete("/product").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.put("/product").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing parameter price"));

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
		req()
				.delete("/product").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 300)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(300))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0));

		Map<Integer, LinkedHashMap> m = req().when().get("/product").getBody().jsonPath().getMap("", Integer.class, LinkedHashMap.class);
		Assertions.assertFalse(m.isEmpty());

		int productID = 1;
		for (Map.Entry<Integer, LinkedHashMap> entry: m.entrySet()) {
			productID = (Integer) entry.getValue().get("productID");
			break;
		}

		req()
				.delete("/product/" + productID +"a").then()
				.statusCode(Matchers.is(500));
//				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req().get("/product/" + productID).then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.anything())
				.body("price", Matchers.equalTo(300))
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
