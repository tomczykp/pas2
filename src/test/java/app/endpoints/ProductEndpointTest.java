package app.endpoints;

import app.model.Product;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
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
				.body(Matchers.containsString("java.lang.NumberFormatException"));

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
				.body(Matchers.containsString("java.lang.NumberFormatException"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200))
				.body("id", Matchers.anything())
				.body("reservations", Matchers.hasSize(0));
	}

	@Test
	public void deleteTest() {
		req()
				.delete("/product").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.delete("/product/6a").then()
				.statusCode(Matchers.is(500))
				.body(Matchers.containsString("java.lang.NumberFormatException"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 300)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(300))
				.body("id", Matchers.anything())
				.body("reservations", Matchers.hasSize(0));

		Map<Integer, Product> m = req().when().get("/product").getBody().jsonPath().getMap("");
		Assertions.assertFalse(m.isEmpty());

		// to jest abominacja !!!!!!!!!!!!
		long id = 1;
		for (Map.Entry<Integer, Product> entry: m.entrySet()) {
			id = Integer.parseInt(String.valueOf(entry.getKey()));
			break;
		}

		req().get("/product/" + id).then()
				.statusCode(Matchers.is(200))
				.body("id", Matchers.anything())
				.body("price", Matchers.anything())
				.body("resevations", Matchers.anything());

		req()
				.delete("/product/" + id).then()
				.body("status", Matchers.equalTo("deletion succesful"));

		req()
				.get("/product/" + id).then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));
	}

}
