package app.endpoints;

import app.dto.CustomerDTO;
import app.exceptions.NotFoundException;
import app.managers.CustomerManager;
import app.model.Customer;
import app.model.Reservation;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

@Path("/customer")
public class CustomerEndpoint {

	@Inject
	private CustomerManager customerManager;

	/**
	 * @param id customer number
	 * @return Response
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Customer customer = (Customer) customerManager.get(Integer.parseInt(id));
			return Response.ok(new CustomerDTO(customer)).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put (@NotNull Customer customer) {
		try {
			CustomerDTO c = customerManager.create(customer);
			return Response.ok(c).build();
		} catch (Exception e) {
			return Response.ok(e).status(409).build();
		}
	}

	private List<Reservation> mapDTO (List<Reservation> reservations) {
		List<Reservation> res = new ArrayList<>();
		for (Reservation r : reservations)
			res.add(r);
		return res;
	}

	@GET
	@Path("/{id}/reservations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReservations (@PathParam("id") String id) {
		try {
			Customer customer = customerManager.get(Integer.parseInt(id));
			List<Reservation> res;
			res = customer.getFutureReservations();

			return Response.ok(mapDTO(res)).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}
}
