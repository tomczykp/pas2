package app.endpoints;

import app.dto.CustomerDTO;
import app.managers.CustomerManager;
import app.model.Customer;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

@Path("/customer")
public class CustomerEndpoint {

	@Inject
	private CustomerManager manager;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));
			if (customer == null)
				return Response.ok(
						new JSONObject().put("status", "customer not found").toString())
						.status(404).build();
			return Response.ok(customer).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@QueryParam("exact") String exact) {
		Map<Integer, Customer> data = manager.getMap();
		if (exact == null || exact.equals(""))
			return Response.ok(data).build();
		return Response.ok(data.get(0)).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response put(@FormParam("email") String email,
						@FormParam("username") String username,
						@FormParam("password") String password) {

		if ( Objects.equals(email, "") || email == null)
			return Response.ok(
					new JSONObject().put("status", "missing arguments 'email'").toString())
					.status(404).build();

		if (Objects.equals(username, "") || username == null)
			return Response.ok(
					new JSONObject().put("status", "missing arguments 'username'").toString())
					.status(404).build();

		try {
			Customer product = manager.create(username, email, password);
			return Response.ok(new CustomerDTO(product)).build();
		} catch (Exception e) {
			return Response.ok(e).status(500).build();
		}

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

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAll() {
		manager.getMap().clear();
		return Response.ok(
						new JSONObject().put("status", "Successfull clearing").toString())
				.build();
	}

}
