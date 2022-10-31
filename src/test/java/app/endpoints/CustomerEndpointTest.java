package app.endpoints;

import app.model.Customer;
import app.model.Product;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", "emaia")
				.put("/customer").then()
				.statusCode(500)
				.body("message", Matchers.is("Username already exist"));
	}

	@Test
	public void deleteTest() {
		req()
				.delete("/customer").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		req()
				.delete("/customer/6a").then()
				.statusCode(Matchers.is(500))
				.body(Matchers.containsString("java.lang.NumberFormatException"));

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

		Map<Integer, Customer> m = req().when().get("/customer").getBody().jsonPath().getMap("");
		Assertions.assertFalse(m.isEmpty());

		// to jest abominacja !!!!!!!!!!!!
		long id = 1;
		for (Map.Entry<Integer, Customer> entry: m.entrySet()) {
			id = Integer.parseInt(String.valueOf(entry.getKey()));
			break;
		}

		req().get("/customer/" + id).then()
				.statusCode(Matchers.is(200))
				.body("id", Matchers.anything())
				.body("email", Matchers.equalTo("emai@a.com"))
				.body("username", Matchers.equalTo("emaia"))
				.body("reservations", Matchers.hasSize(0));

		req()
				.delete("/customer/" + id).then()
				.body("status", Matchers.equalTo("deletion succesful"));

		req()
				.get("/customer/" + id).then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("customer not found"));
	}

	@Test
	public void getAllTest() {
		req()
				.delete("/customer").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		Map<Integer, Customer> m = req().when().get("/customer").getBody().jsonPath().getMap("");
		Assertions.assertTrue(m.isEmpty());

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", "user0")
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("id", Matchers.anything())
				.body("email", Matchers.equalTo("emai@a.com"))
				.body("username", Matchers.equalTo("user0"))
				.body("reservations", Matchers.hasSize(0));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", "user1")
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("id", Matchers.anything())
				.body("email", Matchers.equalTo("emai@a.com"))
				.body("username", Matchers.equalTo("user1"))
				.body("reservations", Matchers.hasSize(0));
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", "user2")
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("id", Matchers.anything())
				.body("email", Matchers.equalTo("emai@a.com"))
				.body("username", Matchers.equalTo("user2"))
				.body("reservations", Matchers.hasSize(0));

		m = req().when().get("/customer").getBody().jsonPath().getMap("");
		Assertions.assertFalse(m.isEmpty());
		Assertions.assertEquals(3, m.size());

		for (Map.Entry<Integer, Customer> entry: m.entrySet()) {
			Customer c = entry.getValue();
			Assertions.assertEquals(Customer.class, c.getClass());
		}

	}


}
