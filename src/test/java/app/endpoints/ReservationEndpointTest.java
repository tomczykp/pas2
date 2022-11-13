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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationEndpointTest {

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

	private List<JsonPath> productIDs;
	private List<JsonPath> customerIDs;
	private List<JsonPath> reservations;

	@BeforeAll
	public void init () {
		productIDs = new ArrayList<>();
		customerIDs = new ArrayList<>();
		List<String> emails = new ArrayList<>();
		List<String> usernames = new ArrayList<>();
		String uniq = LocalDateTime.now().toString();
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
				.body("reservations", Matchers.equalTo(0))
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
				.body("reservations", Matchers.equalTo(0))
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
				.body("reservations", Matchers.equalTo(0))
				.extract().jsonPath());

		productIDs.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.equalTo(0))
				.extract().response().jsonPath());

		productIDs.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 300)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(300))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.equalTo(0))
				.extract().response().jsonPath());

		reservations.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-02-01")
				.formParam("edate", "2023-02-06")
				.formParam("pid", (int) productIDs.get(0).get("productID"))
				.formParam("cid", (int) customerIDs.get(0).get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("product", Matchers.equalTo((int) productIDs.get(0).get("productID")))
				.body("customer", Matchers.equalTo((int) customerIDs.get(0).get("customerID")))
				.body("startDate", Matchers.equalTo("2023-02-01"))
				.body("endDate", Matchers.equalTo("2023-02-06"))
				.body("reservationID", Matchers.anything()).extract().jsonPath());

		reservations.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-02-07")
				.formParam("edate", "2023-02-23")
				.formParam("pid", (int) productIDs.get(0).get("productID"))
				.formParam("cid", (int) customerIDs.get(1).get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("product", Matchers.equalTo((int) productIDs.get(0).get("productID")))
				.body("customer", Matchers.equalTo((int) customerIDs.get(1).get("customerID")))
				.body("startDate", Matchers.equalTo("2023-02-07"))
				.body("endDate", Matchers.equalTo("2023-02-23"))
				.body("reservationID", Matchers.anything()).extract().jsonPath());

	}

	@Test
	public void getTest () {
		JsonPath res = req()
				.get("/reservation").then()
				.statusCode(Matchers.is(200)).extract().jsonPath();

		for (JsonPath c : reservations) {
			int id = c.get("reservationID");
			LinkedHashMap check = res.get(String.valueOf(id));
			Assertions.assertEquals(c.get("customer"), check.get("customer"));
			Assertions.assertEquals(c.get("endDate"), check.get("endDate"));
			Assertions.assertEquals(c.get("startDate"), check.get("startDate"));
			Assertions.assertEquals(c.get("reservationID"), check.get("reservationID"));
			Assertions.assertEquals(c.get("product"), check.get("product"));
		}

		req()
				.get("/reservation/-3").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("reservation not found"));

		int lastID = (int) reservations.get(reservations.size() - 1).get("reservationID") + 90;
		req()
				.get("/reservation/" + lastID).then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("reservation not found"));

		req()
				.get("/reservation/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));
	}

	@Test
	public void invalidMissingDeleteTests () {

		req()
				.delete("/reservation/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		int lastID = (int) (reservations.get(reservations.size() - 1).get("reservationID")) + 90;
		req()
				.delete("/reservation/" + lastID).then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("reservation not found"));

	}

	@Test
	public void deleteReservedTest () {
		int pid = productIDs.get(1).get("productID");
		int cid = customerIDs.get(1).get("customerID");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		JsonPath t = req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", LocalDate.now().format(dateTimeFormatter))
				.formParam("edate", LocalDate.now().plusDays(1).format(dateTimeFormatter))
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("product", Matchers.equalTo(pid))
				.body("customer", Matchers.equalTo(cid))
				.body("startDate", Matchers.equalTo(LocalDate.now().format(dateTimeFormatter)))
				.body("endDate", Matchers.equalTo(LocalDate.now().plusDays(1).format(dateTimeFormatter)))
				.body("reservationID", Matchers.anything()).extract().jsonPath();

		req()
				.delete("/reservation/" + t.get("reservationID")).then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("cannot remove already started reservation"));

		t = req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", LocalDate.now().plusDays(1).format(dateTimeFormatter))
				.formParam("edate", LocalDate.now().plusDays(5).format(dateTimeFormatter))
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("product", Matchers.equalTo(pid))
				.body("customer", Matchers.equalTo(cid))
				.body("startDate", Matchers.equalTo(LocalDate.now().plusDays(1).format(dateTimeFormatter)))
				.body("endDate", Matchers.equalTo(LocalDate.now().plusDays(5).format(dateTimeFormatter)))
				.body("reservationID", Matchers.anything()).extract().jsonPath();

		req()
				.delete("/reservation/" + t.get("reservationID")).then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("cannot remove future reservation"));

	}

	@Test
	public void missingArgPutTests () {

		int cid = customerIDs.get(0).get("customerID");
		int pid = productIDs.get(0).get("productID");

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("edate", "2022-12-23")
				.formParam("cid", cid)
				.formParam("pid", pid)
				.put("/reservation").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing arguments 'sdate'"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("cid", cid)
				.formParam("pid", pid)
				.put("/reservation").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing arguments 'edate'"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing arguments 'pid'"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("pid", pid)
				.put("/reservation").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing arguments 'cid'"));
	}

	@Test
	public void invalidPidCidPutTests () {

		int cid = customerIDs.get(customerIDs.size() - 1).get("customerID");
		int pid = productIDs.get(productIDs.size() - 1).get("productID");

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("pid", pid + 1)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid + 1)
				.put("/reservation").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("customer not found"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("pid", pid + "o")
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid + "o")
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

	}

	@Test
	public void invalidDatePutTests () {

		int cid = customerIDs.get(customerIDs.size() - 1).get("customerID");
		int pid = productIDs.get(productIDs.size() - 1).get("productID");

		// month
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-12-06")
				.formParam("edate", "2023-13-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-13-06")
				.formParam("edate", "2023-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));


		// day
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-12-33")
				.formParam("edate", "2024-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-12-06")
				.formParam("edate", "2023-13-33")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		// month with only 30 days
//		req()
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.formParam("sdate", "2023-06-31")
//				.formParam("edate", "2023-12-23")
//				.formParam("pid", pid)
//				.formParam("cid", cid)
//				.put("/reservation").then()
//				.statusCode(Matchers.is(500))
//				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		// format
		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "202312-06")
				.formParam("edate", "2023-04-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-12-06")
				.formParam("edate", "2023-1323")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-2-06")
				.formParam("edate", "2023-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-01-06")
				.formParam("edate", "2023-3-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-01-06")
				.formParam("edate", "2023-03-3")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-01-6")
				.formParam("edate", "2023-03-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		// dla lutego
//		req()
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.formParam("sdate", "2023-02-29")
//				.formParam("edate", "2023-06-23")
//				.formParam("pid", pid)
//				.formParam("cid", cid)
//				.put("/reservation").then()
//				.statusCode(Matchers.is(500))
//				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));
//
//		req()
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.formParam("sdate", "2023-02-31")
//				.formParam("edate", "2023-06-23")
//				.formParam("pid", pid)
//				.formParam("cid", cid)
//				.put("/reservation").then()
//				.statusCode(Matchers.is(500))
//				.body("stackTrace.className", Matchers.hasItem("java.time.format.DateTimeFormatter"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2024-02-28")
				.formParam("edate", "2024-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("product", Matchers.equalTo(pid))
				.body("customer", Matchers.equalTo(cid))
				.body("startDate", Matchers.equalTo("2024-02-28"))
				.body("endDate", Matchers.equalTo("2024-12-23"))
				.body("reservationID", Matchers.anything());

	}

	@Test
	public void invalidDateRangesTests () {

		int cid = customerIDs.get(customerIDs.size() - 1).get("customerID");
		int pid = productIDs.get(productIDs.size() - 1).get("productID");

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "1999-02-28")
				.formParam("edate", "2023-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("cannot make reservation in the past"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "1999-02-28")
				.formParam("edate", "1999-12-23")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("cannot make reservation in the past"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-02-05")
				.formParam("edate", "2023-02-01")
				.formParam("pid", pid)
				.formParam("cid", cid)
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("end date cannot be before start date"));
	}

	@Test
	public void alredyReservedTests () {
		JsonPath product = productIDs.get(0);
		JsonPath customer = customerIDs.get(0);

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-03-07")
				.formParam("edate", "2023-03-23")
				.formParam("pid", (int) product.get("productID"))
				.formParam("cid", (int) customer.get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("product", Matchers.equalTo((int) product.get("productID")))
				.body("customer", Matchers.equalTo((int) customer.get("customerID")))
				.body("startDate", Matchers.equalTo("2023-03-07"))
				.body("endDate", Matchers.equalTo("2023-03-23"))
				.body("reservationID", Matchers.anything());

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-03-07")
				.formParam("edate", "2023-03-23")
				.formParam("pid", (int) product.get("productID"))
				.formParam("cid", (int) customer.get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("product is already reserved"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-03-01")
				.formParam("edate", "2023-03-08")
				.formParam("pid", (int) product.get("productID"))
				.formParam("cid", (int) customer.get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("product is already reserved"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-03-21")
				.formParam("edate", "2023-03-29")
				.formParam("pid", (int) product.get("productID"))
				.formParam("cid", (int) customer.get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("product is already reserved"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-03-09")
				.formParam("edate", "2023-03-20")
				.formParam("pid", (int) product.get("productID"))
				.formParam("cid", (int) customer.get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("product is already reserved"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2023-03-02")
				.formParam("edate", "2023-03-30")
				.formParam("pid", (int) product.get("productID"))
				.formParam("cid", (int) customer.get("customerID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("product is already reserved"));

	}

}
