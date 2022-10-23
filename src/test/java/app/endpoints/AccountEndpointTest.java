package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class AccountEndpointTest {

	private static final RequestSpecification req = RestAssured.given()
			.baseUri("http://localhost")
			.basePath("/rest/api")
			.port(8080)
			.contentType(ContentType.JSON);
	@Test
	public void getTest() {
		req
				.get("/account").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.anyOf(Matchers.is("{}")));

		req
				.queryParam("id", "param")
				.get("/account").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.is("null: hello : param"));
	}

	@Test
	public void putTest() {
		req
				.put("/account").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.is("Could not create user, missing parameters"));

		req
				.queryParam("name","Jan")
				.queryParam("surname","Kowalski")
				.put("/account").then()
				.statusCode(Matchers.is(200));

	}

}
