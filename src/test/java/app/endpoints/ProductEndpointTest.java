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
				.get("/product").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.anyOf(Matchers.is("{}")));

	}

	@Test
	public void putTest() {
		req
				.put("/account").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.is("Could not create product, missing parameter, price"));

	}

}
