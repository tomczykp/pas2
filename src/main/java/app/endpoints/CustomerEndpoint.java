package app.endpoints.a;

import app.managers.a.CustomerManager;
import app.model.a.Customer;
import app.model.a.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

@Path("/customer")
@ApplicationScoped
public class CustomerEndpoint {

	@Inject
	private CustomerManager manager;
	public CustomerEndpoint() {}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));
			if (customer == null)
				return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
			return Response.ok(customer).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@QueryParam("exact") String exact) {
		Map<Integer, Customer> data = this.manager.getMap();
		if (exact == null || exact.equals(""))
			return Response.ok(data).build();
		return Response.ok(data.get(0)).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(@QueryParam("username") String u, @QueryParam("email") String e) {
		if ( Objects.equals(e, "") || e == null)
			return Response.ok("{'status':'missing arguments `email` '}")
					.status(404).build();

		if (Objects.equals(u, "") || u == null)
			return Response.ok("{'status':'missing arguments `username` '}")
					.status(404).build();

		Customer product = manager.create(u, e);
		return Response.ok(product).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {
		if (Objects.equals(id, "") || id == null)
			return Response.ok(
							new JSONObject().put("status", "missing parameter id").toString())
					.status(404).build();

		try {
			int t = Integer.parseInt(id);
			manager.delete(t);
			return Response.ok(new JSONObject().put("status", "deletion succesful").toString()).build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).status(500).build();
		}
	}

}
