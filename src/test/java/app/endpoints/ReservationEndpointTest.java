package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationEndpointTest {

	private RequestSpecification req () {
		return RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/rest/api")
				.port(8080);
	}

	private List<Integer> getProductIDs () {
		Map<Integer, LinkedHashMap> m = req().when().get("/product").getBody().jsonPath().getMap("", Integer.class, LinkedHashMap.class);
		List<Integer> list = new ArrayList<>();
		for (Map.Entry<Integer, LinkedHashMap> entry: m.entrySet())
			list.add((Integer) entry.getValue().get("productID"));
		list.sort(Comparator.naturalOrder());
		return list;
	}

	private List<Integer> getCustomerIDs () {
		Map<Integer, LinkedHashMap> m = req().when().get("/customer").getBody().jsonPath().getMap("", Integer.class, LinkedHashMap.class);
		List<Integer> list = new ArrayList<>();
		for (Map.Entry<Integer, LinkedHashMap> entry: m.entrySet())
			list.add((Integer) entry.getValue().get("customerID"));
		list.sort(Comparator.naturalOrder());
		return list;
	}

	private List<JsonPath> productIDs;
	private List<JsonPath> customerIDs;
	private List<JsonPath> reservations;
	private List<String> emails;
	private List<String> usernames;
	private String uniq;

	@BeforeAll
	public void init() {
		productIDs = new ArrayList<>();
		customerIDs = new ArrayList<>();
		emails = new ArrayList<>();
		usernames = new ArrayList<>();
		uniq = LocalDateTime.now().toString();
		reservations = new ArrayList<>();

		List<Integer> list = getCustomerIDs();
		int k = 1;
		if (!list.isEmpty())
			k = list.get(list.size() - 1) + 1;

		for (int i = 0; i < 2; i++) {
			emails.add(uniq + "email" + k + "@m.com");
			usernames.add(uniq + "user" + k++);
		}
		emails.add("email@com.pl");
		usernames.add(uniq + "login" + k);

		customerIDs.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails.get(0))
				.formParam("username", usernames.get(0))
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails.get(0)))
				.body("username", Matchers.equalTo(usernames.get(0)))
				.body("reservations", Matchers.hasSize(0))
				.extract().jsonPath());

		customerIDs.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails.get(1))
				.formParam("username", usernames.get(1))
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails.get(1)))
				.body("username", Matchers.equalTo(usernames.get(1)))
				.body("reservations", Matchers.hasSize(0))
				.extract().jsonPath());

		customerIDs.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails.get(2))
				.formParam("username", usernames.get(2))
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails.get(2)))
				.body("username", Matchers.equalTo(usernames.get(2)))
				.body("reservations", Matchers.hasSize(0))
				.extract().jsonPath());

		productIDs.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0))
				.extract().response().jsonPath());

		productIDs.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 300)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(300))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0))
				.extract().response().jsonPath());


		reservations.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("", "")
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("reservationID", Matchers.anything())
				.extract().response().jsonPath());
	}

	@Test
	public void getTest() {

		req()
				.get("/reservation").then()
				.statusCode(Matchers.is(200))
				.body(Matchers.anything());

		req()
				.get("/reservation/1").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("reservation not found"));

		req()
				.get("/reservation/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));
	}

	@Test
	public void putTest() {

		int i = 0;
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.put("/reservation").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing parameters"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("cid", 1)
				.formParam("pid", 1)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("message", Matchers.equalTo("object not found"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("cid", (int)customerIDs.get(0).get("customerID"))
				.formParam("pid", (int)productIDs.get(0).get("productID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(200));
//				.body("message", Matchers.equalTo("object not found"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0));
	}

}
