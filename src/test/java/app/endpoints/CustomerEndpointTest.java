package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class CustomerEndpointTest {

	private RequestSpecification req() {
		return RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/rest/api")
				.port(8080);
	}

	@Test
	public void getTest() {
		req()
				.delete("/customer").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.get("/customer").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.is("{}"));

		req()
				.get("/customer/1").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("customer not found"));

		req()
				.get("/customer/1o").then()
				.statusCode(Matchers.is(500))
				.body(Matchers.containsString("java.lang.NumberFormatException"));

	}

	@Test
	public void putTest() {
		req()
				.delete("/customer").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.put("/customer").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing arguments 'username'"));


		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("username", "username")
				.put("/customer").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing arguments 'email'"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", "emaia")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("id", Matchers.anything())
				.body("email", Matchers.equalTo("emai@a.com"))
				.body("username", Matchers.equalTo("emaia"))
				.body("reservations", Matchers.hasSize(0));
	}

}
