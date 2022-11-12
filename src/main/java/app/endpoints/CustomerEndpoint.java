package app.endpoints;

import app.dto.CustomerDTO;
import app.exceptions.NotFoundException;
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

	/**
	 * @param username: to match or search exactly
	 * @param exact:    whether to search or match
	 * @return Response, Map of matching data
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll (
			@QueryParam("username") String username,
			@QueryParam("exact") String exact) {
		Map<Integer, Customer> data;

		if (Objects.equals(username, "") || username == null)
			data = manager.getMap();
		else if (exact == null || exact.equals(""))
			data =
					manager.get((Customer c) -> (c.getUsername().contains(username)) || username.contains(c.getUsername()));
		else
			data = manager.get((Customer c) -> Objects.equals(c.getUsername(), username));

		return Response.ok(data).build();
	}

	/**
	 * @param id customer number
	 * @return Response
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));
			return Response.ok(customer).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(500).build();
		}
	}

	@GET
	@Path("/{id}/reservations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReservations (@PathParam("id") String id, @QueryParam("past") boolean fromPast) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));

			if (fromPast)
				return Response.ok(customer.getPastReservations()).build();
			else
				return Response.ok(customer.getFutureReservations()).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(500).build();
		}
	}

	@PATCH
	@Path("/{id}/activate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setActive (@PathParam("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));
			customer.setActive(true);
			return Response.ok(
					new JSONObject().put("status", "set to active")
							.toString()).status(200).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(500).build();
		}
	}

	@PATCH
	@Path("/{id}/deactivate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setDeactivate (@PathParam("id") String id) {
		try {
			Customer customer = manager.get(Integer.parseInt(id));

			if (customer.isReserved())
				throw new Exception("cannot deactivate user with ongoing reservations");

			customer.setActive(false);
			return Response.ok(
					new JSONObject().put("status", "set to deactive")
							.toString()).status(200).build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(500).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response put (@FormParam("email") String email,
						 @FormParam("username") String username,
						 @FormParam("password") String password) {

		if (Objects.equals(email, "") || email == null)
			return Response.ok(
							new JSONObject().put("status", "missing arguments 'email'").toString())
					.status(404).build();

		if (Objects.equals(username, "") || username == null)
			return Response.ok(
							new JSONObject().put("status", "missing arguments 'username'").toString())
					.status(404).build();

		try {
			CustomerDTO customer = manager.create(username, email, password);
			return Response.ok(customer).build();
		} catch (Exception e) {
			return Response.ok(e).status(500).build();
		}

	}

}
