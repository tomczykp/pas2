package app.endpoints;

import app.model.Customer;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
				.body(Matchers.equalTo("{}"));

		req()
				.get("/customer/1").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("customer not found"));

		req()
				.get("/customer/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

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
				.body("customerID", Matchers.anything())
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
	public void getAllTest() {
		req()
				.delete("/customer").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		Map<Integer, Customer> m = req().when().get("/customer").getBody().jsonPath().getMap("");
		Assertions.assertTrue(m.isEmpty());

		String[] emails = {"email1@a.com", "email2@a.com", "email3@a.com"};
		String[] usernames = {"user1", "user2", "user3"};
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[0])
				.formParam("username", usernames[0])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[0]))
				.body("username", Matchers.equalTo(usernames[0]))
				.body("reservations", Matchers.hasSize(0));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[1])
				.formParam("username", usernames[1])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[1]))
				.body("username", Matchers.equalTo(usernames[1]))
				.body("reservations", Matchers.hasSize(0));
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[2])
				.formParam("username", usernames[2])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[2]))
				.body("username", Matchers.equalTo(usernames[2]))
				.body("reservations", Matchers.hasSize(0));

		// all test
		JsonPath dane = req().when().get("/customer").getBody().jsonPath();
		Assertions.assertFalse(dane.getMap("").isEmpty());
		Assertions.assertEquals(3, dane.getMap("").size());

		int i = 0;
		for (Map.Entry<Integer, LinkedHashMap> entry: dane.getMap("", Integer.class, LinkedHashMap.class).entrySet()) {
			LinkedHashMap<String, Object> tmp = entry.getValue();
			Assertions.assertNotNull(tmp.get("customerID"));
			Assertions.assertEquals(emails[i], tmp.get("email"));
			Assertions.assertEquals(usernames[i], tmp.get("username"));
			Assertions.assertEquals(new ArrayList<>(), tmp.get("reservations"));
			i++;
		}
	}

	@Test
	public void getExactUsername() {
		req()
				.delete("/customer").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		Map<Integer, Customer> m = req().when().get("/customer").getBody().jsonPath().getMap("");
		Assertions.assertTrue(m.isEmpty());

		String[] emails = {"email1@a.com", "email2@a.com", "email3@a.com"};
		String[] usernames = {"user1", "login1", "user2"};
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[0])
				.formParam("username", usernames[0])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[0]))
				.body("username", Matchers.equalTo(usernames[0]))
				.body("reservations", Matchers.hasSize(0));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[1])
				.formParam("username", usernames[1])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[1]))
				.body("username", Matchers.equalTo(usernames[1]))
				.body("reservations", Matchers.hasSize(0));
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[2])
				.formParam("username", usernames[2])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[2]))
				.body("username", Matchers.equalTo(usernames[2]))
				.body("reservations", Matchers.hasSize(0));

		// all test
		JsonPath dane = req().when()
				.queryParam("exact", "1")
				.queryParam("username", usernames[0])
				.get("/customer")
				.getBody().jsonPath();

		Assertions.assertFalse(dane.getMap("").isEmpty());
		Assertions.assertEquals(1, dane.getMap("").size());

		for (Map.Entry<Integer, LinkedHashMap> entry: dane.getMap("", Integer.class, LinkedHashMap.class).entrySet()) {
			LinkedHashMap<String, Object> tmp = entry.getValue();
			Assertions.assertNotNull(tmp.get("customerID"));
			Assertions.assertEquals(emails[0], tmp.get("email"));
			Assertions.assertEquals(usernames[0], tmp.get("username"));
			Assertions.assertEquals(new ArrayList<>(), tmp.get("reservations"));
		}

		dane = req().when()
				.queryParam("username", "user")
				.get("/customer")
				.getBody().jsonPath();

		Assertions.assertFalse(dane.getMap("").isEmpty());
		Assertions.assertEquals(2, dane.getMap("").size());

		int i = 0;
		for (Map.Entry<Integer, LinkedHashMap> entry: dane.getMap("", Integer.class, LinkedHashMap.class).entrySet()) {
			LinkedHashMap<String, Object> tmp = entry.getValue();
			Assertions.assertNotNull(tmp.get("customerID"));
			Assertions.assertEquals(emails[i], tmp.get("email"));
			Assertions.assertEquals(usernames[i], tmp.get("username"));
			Assertions.assertEquals(new ArrayList<>(), tmp.get("reservations"));
			i+=2;
		}
	}

	@Test
	public void getMatchUsername() {
		req()
				.delete("/customer").then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("Successfull clearing"));

		Map<Integer, Customer> m = req().when().get("/customer").getBody().jsonPath().getMap("");
		Assertions.assertTrue(m.isEmpty());

		String[] emails = {"email1@a.com", "email2@a.com", "email3@a.com"};
		String[] usernames = {"user1", "login1", "user2"};
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[0])
				.formParam("username", usernames[0])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[0]))
				.body("username", Matchers.equalTo(usernames[0]))
				.body("reservations", Matchers.hasSize(0));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[1])
				.formParam("username", usernames[1])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[1]))
				.body("username", Matchers.equalTo(usernames[1]))
				.body("reservations", Matchers.hasSize(0));
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails[2])
				.formParam("username", usernames[2])
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails[2]))
				.body("username", Matchers.equalTo(usernames[2]))
				.body("reservations", Matchers.hasSize(0));

		JsonPath dane = req().when()
				.queryParam("username", "user")
				.get("/customer")
				.getBody().jsonPath();

		Assertions.assertFalse(dane.getMap("").isEmpty());
		Assertions.assertEquals(2, dane.getMap("").size());

		int i = 0;
		for (Map.Entry<Integer, LinkedHashMap> entry: dane.getMap("", Integer.class, LinkedHashMap.class).entrySet()) {
			LinkedHashMap<String, Object> tmp = entry.getValue();
			Assertions.assertNotNull(tmp.get("customerID"));
			Assertions.assertEquals(emails[i], tmp.get("email"));
			Assertions.assertEquals(usernames[i], tmp.get("username"));
			Assertions.assertEquals(new ArrayList<>(), tmp.get("reservations"));
			i+=2;
		}
	}
}
