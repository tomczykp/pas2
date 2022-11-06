package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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

	private List<Integer> productIDs;
	private List<Integer> customerIDs;

	@BeforeAll
	public void init() {
		productIDs = getProductIDs();
		customerIDs = getCustomerIDs();
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
				.formParam("cid", customerIDs.get(i))
				.formParam("pid", productIDs.get(i++))
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
