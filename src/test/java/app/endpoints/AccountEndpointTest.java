package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.hamcrest.Matchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class AccountEndpointTest {

	private static 	RequestSpecification req = RestAssured.given().baseUri("http://localhost").basePath("/rest/api").port(8080);
	@Test
	public void getTest() {
		req.get("/account").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.anyOf(Matchers.is("{}")));
	}
}
