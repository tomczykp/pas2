package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerEndpointTest {

	private String uniq;

	private RequestSpecification req () {
		return RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/rest/api")
				.port(8080);
	}


	private List<Integer> getCustomerIDs () {
		Map<Integer, LinkedHashMap> m = req().when().get("/customer").getBody().jsonPath().getMap("", Integer.class,
				LinkedHashMap.class);
		List<Integer> list = new ArrayList<>();
		for (Map.Entry<Integer, LinkedHashMap> entry : m.entrySet())
			list.add((Integer) entry.getValue().get("customerID"));
		list.sort(Comparator.naturalOrder());
		return list;
	}

	List<JsonPath> ids;

	List<String> emails;
	List<String> usernames;

	@BeforeAll
	public void init () {
		ids = new ArrayList<>();
		emails = new ArrayList<>();
		usernames = new ArrayList<>();
		uniq = LocalDateTime.now().toString();

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

		ids.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails.get(0))
				.formParam("username", usernames.get(0))
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("active", Matchers.equalTo(true))
				.body("email", Matchers.equalTo(emails.get(0)))
				.body("username", Matchers.equalTo(usernames.get(0)))
				.body("reservations", Matchers.equalTo(0))
				.extract().jsonPath());

		ids.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails.get(1))
				.formParam("username", usernames.get(1))
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("active", Matchers.equalTo(true))
				.body("email", Matchers.equalTo(emails.get(1)))
				.body("username", Matchers.equalTo(usernames.get(1)))
				.body("reservations", Matchers.equalTo(0))
				.extract().jsonPath());

		ids.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", emails.get(2))
				.formParam("username", usernames.get(2))
				.formParam("password", "haslo")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("active", Matchers.equalTo(true))
				.body("customerID", Matchers.anything())
				.body("email", Matchers.equalTo(emails.get(2)))
				.body("username", Matchers.equalTo(usernames.get(2)))
				.body("reservations", Matchers.equalTo(0))
				.extract().jsonPath());
	}

	@Test
	public void getTest () {

		JsonPath res = req()
				.get("/customer").then()
				.statusCode(Matchers.is(200)).extract().jsonPath();

		for (JsonPath c : ids) {
			int id = c.get("customerID");
			LinkedHashMap check = res.get(String.valueOf(id));
			Assertions.assertEquals(c.get("username"), check.get("username"));
			Assertions.assertEquals(c.get("active"), check.get("active"));
			Assertions.assertEquals(c.get("customerID"), check.get("customerID"));
			Assertions.assertEquals(c.get("email"), check.get("email"));
			Assertions.assertEquals(c.get("reservations"), check.get("reservations"));
		}

		req()
				.get("/customer/-4").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("customer not found"));

		req()
				.get("/customer/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req()
				.get("/customer/" + ids.get(2).get("customerID")).then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("active", Matchers.equalTo(true))
				.body("email", Matchers.equalTo(emails.get(2)))
				.body("username", Matchers.equalTo(usernames.get(2)))
				.body("reservations", Matchers.equalTo(0));
	}

	@Test
	public void putTest () {

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

		int k = (int) (ids.get(ids.size() - 1).get("customerID")) + 1;
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", "emaia" + k)
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("active", Matchers.equalTo(true))
				.body("email", Matchers.equalTo("emai@a.com"))
				.body("username", Matchers.equalTo("emaia" + k))
				.body("reservations", Matchers.equalTo(0));


		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", "emaia" + k)
				.put("/customer").then()
				.statusCode(500)
				.body("message", Matchers.is("Username already exist"));

	}

	@Test
	public void getExactUsername () {

		JsonPath dane = req().when()
				.queryParam("exact", "1")
				.queryParam("username", usernames.get(0))
				.get("/customer")
				.getBody().jsonPath();

		Assertions.assertFalse(dane.getMap("").isEmpty());
		Assertions.assertEquals(1, dane.getMap("").size());

		for (Map.Entry<Integer, LinkedHashMap> entry :
				dane.getMap("", Integer.class, LinkedHashMap.class).entrySet()) {
			LinkedHashMap<String, Object> tmp = entry.getValue();
			Assertions.assertNotNull(tmp.get("customerID"));
			Assertions.assertEquals(emails.get(0), tmp.get("email"));
			Assertions.assertEquals(usernames.get(0), tmp.get("username"));
			Assertions.assertEquals(0, tmp.get("reservations"));
		}
	}

	@Test
	public void getMatchUsername () {

		JsonPath dane = req().when()
				.queryParam("username", uniq + "user")
				.get("/customer")
				.getBody().jsonPath();

		Assertions.assertFalse(dane.getMap("").isEmpty());

		int i = 0;
		for (Map.Entry<Integer, LinkedHashMap> entry :
				dane.getMap("", Integer.class, LinkedHashMap.class).entrySet()) {
			LinkedHashMap<String, Object> tmp = entry.getValue();
			Assertions.assertNotNull(tmp.get("customerID"));
			Assertions.assertEquals(emails.get(i), tmp.get("email"));
			Assertions.assertEquals(usernames.get(i), tmp.get("username"));
			Assertions.assertEquals(0, tmp.get("reservations"));
			i++;
		}
	}

	@Test
	public void activateDeactivateTest () {

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.patch("/customer/-4/activate").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("customer not found"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.patch("/customer/1o/activate").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.patch("/customer/499999/deactivate").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("customer not found"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.patch("/customer/1o/deactivate").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));


		int id = ids.get(0).get("customerID");
		req().when()
				.get("/customer/" + id)
				.then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.equalTo(id))
				.body("active", Matchers.equalTo(true))
				.body("email", Matchers.equalTo(emails.get(0)))
				.body("username", Matchers.equalTo(usernames.get(0)))
				.body("reservations", Matchers.equalTo(0));

		req().when()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.patch("/customer/" + id + "/deactivate")
				.then()
				.body("status", Matchers.equalTo("set to deactive"));


		req().when()
				.get("/customer/" + id)
				.then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.equalTo(id))
				.body("active", Matchers.equalTo(false))
				.body("email", Matchers.equalTo(emails.get(0)))
				.body("username", Matchers.equalTo(usernames.get(0)))
				.body("reservations", Matchers.equalTo(0));

		req().when()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.patch("/customer/" + id + "/activate")
				.then()
				.body("status", Matchers.equalTo("set to active"));

		req().when()
				.get("/customer/" + id)
				.then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.equalTo(id))
				.body("active", Matchers.equalTo(true))
				.body("email", Matchers.equalTo(emails.get(0)))
				.body("username", Matchers.equalTo(usernames.get(0)))
				.body("reservations", Matchers.equalTo(0));
	}

	@Test
	public void deactivateReserved () {
		JsonPath productPath = req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.equalTo(0))
				.extract().response().jsonPath();

		int pid = productPath.get("productID");
		int cid = ids.get(1).get("customerID");

		req().when()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.patch("/customer/" + cid + "/deactivate")
				.then()
				.body("status", Matchers.equalTo("set to deactive"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("cid", cid)
				.formParam("pid", pid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("customer has active reservations or is inactive"));

		req()
				.get("/customer/" + cid).then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("active", Matchers.equalTo(false))
				.body("email", Matchers.equalTo(ids.get(1).get("email")))
				.body("username", Matchers.equalTo(ids.get(1).get("username")))
				.body("reservations", Matchers.equalTo(0));

		req()
				.get("/product/" + pid).then()
				.statusCode(200)
				.body("price", Matchers.equalTo(productPath.get("price")))
				.body("productID", Matchers.equalTo(productPath.get("productID")))
				.body("reservations", Matchers.equalTo(0));

	}


}
