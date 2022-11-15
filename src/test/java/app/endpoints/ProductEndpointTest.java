package app.endpoints;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductEndpointTest {

	private RequestSpecification req () {
		return RestAssured.given()
				.baseUri("http://localhost")
				.basePath("/rest/api")
				.port(8080);
	}

	List<JsonPath> ids;
	private String uniq;

	@BeforeAll
	public void init () {
		uniq = LocalDateTime.now().toString();
		ids = new ArrayList<>();
		ids.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200.0F))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0))
				.extract().response().jsonPath());

		ids.add(req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 300)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(300.0F))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0))
				.extract().response().jsonPath());
	}

	@Test
	public void getTest () {


		JsonPath res = req()
				.get("/product").then()
				.statusCode(Matchers.is(200)).extract().jsonPath();

		for (JsonPath c : ids) {
			int id = c.get("productID");
			LinkedHashMap check = res.get(String.valueOf(id));
			Assertions.assertEquals(c.get("productID"), check.get("productID"));
			Assertions.assertEquals(c.get("price"), check.get("price"));
			Assertions.assertEquals(c.get("reservations"), check.get("reservations"));
		}

		req()
				.get("/product/-4").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));

		req()
				.get("/product/1o").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

	}

	@Test
	public void putTest () {

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.put("/product").then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("missing parameter 'price'"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", "20a")
				.put("/product").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200.0F))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0));
	}

	@Test
	public void deleteTest () {

		Map<Integer, LinkedHashMap> m = req().when().get("/product").getBody().jsonPath().getMap("", Integer.class,
				LinkedHashMap.class);
		Assertions.assertFalse(m.isEmpty());

		float price = ids.get(0).get("price");
		int productID = ids.get(0).get("productID");

		req().get("/product/" + productID).then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.equalTo(productID))
				.body("price", Matchers.equalTo(price))
				.body("reservations", Matchers.hasSize(0));

		req()
				.delete("/product/" + productID + "a").then()
				.statusCode(Matchers.is(500))
				.body("stackTrace.className", Matchers.hasItem("java.lang.NumberFormatException"));

		req().get("/product/" + productID).then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.equalTo(productID))
				.body("price", Matchers.equalTo(price))
				.body("reservations", Matchers.hasSize(0));

		req()
				.delete("/product/" + productID).then()
				.statusCode(Matchers.is(200))
				.body("status", Matchers.equalTo("deletion succesful"));

		req()
				.delete("/product/" + productID).then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));

		req()
				.get("/product/" + productID).then()
				.statusCode(Matchers.is(404))
				.body("status", Matchers.equalTo("product not found"));
	}

	@Test
	public void deleteReseved () {
		JsonPath cPath = req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("email", "emai@a.com")
				.formParam("username", uniq + "user")
				.put("/customer").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.anything())
				.body("active", Matchers.equalTo(true))
				.body("email", Matchers.equalTo("emai@a.com"))
				.body("username", Matchers.equalTo(uniq + "user"))
				.body("reservations", Matchers.hasSize(0)).extract().jsonPath();

		JsonPath pPath = req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("price", 200)
				.put("/product").then()
				.statusCode(Matchers.is(200))
				.body("price", Matchers.equalTo(200.0F))
				.body("productID", Matchers.anything())
				.body("reservations", Matchers.hasSize(0)).extract().jsonPath();

		req()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.formParam("sdate", "2022-12-06")
				.formParam("edate", "2022-12-23")
				.formParam("cid", (int) cPath.get("customerID"))
				.formParam("pid", (int) pPath.get("productID"))
				.put("/reservation").then()
				.statusCode(Matchers.is(200))
				.body("customerID", Matchers.equalTo((int) cPath.get("customerID")))
				.body("productID", Matchers.equalTo((int) pPath.get("productID")));

		req()
				.delete("/product/" + pPath.get("productID")).then()
				.statusCode(Matchers.is(500))
				.body("status", Matchers.equalTo("cannot delete product with ongoing reservations"));


	}

	@Test
	public void patchTests () {

		JsonPath target = ids.get(1);
		int id = target.get("productID");
		req()
				.get("/product/" + id).then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.equalTo(target.get("productID")))
				.body("price", Matchers.equalTo(target.get("price")))
				.body("reservations", Matchers.equalTo(target.get("reservations")));

		JSONObject data = new JSONObject()
				.put("productID", (int) target.get("productID"))
				.put("price", 333)
				.put("reservations", (ArrayList<Integer>) target.get("reservations"));

		req()
				.contentType(MediaType.APPLICATION_JSON)
				.body(data.toString())
				.patch("/product").then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.equalTo(target.get("productID")))
				.body("price", Matchers.equalTo(333.0F))
				.body("reservations", Matchers.equalTo(target.get("reservations")));

		req()
				.get("/product/" + id).then()
				.statusCode(Matchers.is(200))
				.body("productID", Matchers.equalTo(target.get("productID")))
				.body("price", Matchers.equalTo(333.0F))
				.body("reservations", Matchers.equalTo(target.get("reservations")));
	}

}
