package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class ProductEndpointTest {

	private static final RequestSpecification req = RestAssured.given()
			.baseUri("http://localhost")
			.basePath("/rest/api")
			.port(8080)
			.contentType(ContentType.JSON);
	@Test
	public void getTest() {
		req
				.delete("/product").then()
				.statusCode(200)
				.body("status", Matchers.equalTo("Successfull clearing"));

		req
				.get("/product").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.is("{}"));

		req
				.get("/product/1").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));

		req
				.get("/product/1o").then()
				.statusCode(Matchers.is(500))
				.body(Matchers.containsString("java.lang.NumberFormatException"));

	}

	@Test
	public void putTest() {
		req
				.delete("/product").then()
				.statusCode(200)
				.body("status", Matchers.equalTo("Successfull clearing"));

		req
				.put("/product").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing parameter price"));

		req
				.queryParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200));

	}

}
