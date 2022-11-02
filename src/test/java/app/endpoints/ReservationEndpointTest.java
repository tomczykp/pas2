package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class ReservationEndpointTest {

	private RequestSpecification req () {
		return RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/rest/api")
				.port(8080);
	}

	@Test
	public void getTest() {
		req()
				.delete("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.get("/reservation").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.equalTo("{}"));

		req()
				.get("/reservation/1").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("reservation not found"));

		req()
				.get("/reservation/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

	}

}
