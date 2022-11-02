package app.endpoints;

import app.dto.ReservationDTO;
import app.managers.CustomerManager;
import app.managers.ProductManager;
import app.managers.ReservationManager;
import app.model.Customer;
import app.model.Product;
import app.model.Reservation;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;

@Path("/reservation")
public class ReservationEndpoint {

	@Inject
	private ReservationManager reservationManager;
	@Inject
	private CustomerManager customerManager;
	@Inject
	private ProductManager productManager;

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response put (
			@FormParam("date") String start,
			@FormParam("date") String end,
			@FormParam("cid") String cid,
			@FormParam("pid") String pid) {
		if (Objects.equals(start, "") || Objects.equals(end, "")
				|| Objects.equals(cid, "") || Objects.equals(pid, ""))
			return Response.ok(
					new JSONObject().put("status", "missing arguments")
							.toString()).status(404).build();

		try {
			LocalDateTime endDate = LocalDateTime.parse(end);
			LocalDateTime startDate = LocalDateTime.parse(start);
			Customer customer = customerManager.get(Integer.parseInt(cid));
			Product product = productManager.get(Integer.parseInt(pid));
			Reservation reservation = reservationManager.create(startDate, endDate, customer, product);
			return Response.ok(new ReservationDTO(reservation)).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (Exception e) {
			return Response.ok(new JSONObject().put("status", e.getMessage())
					.toString()).status(500).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll () {
		return Response.ok(reservationManager.getMap()).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Reservation reservation = reservationManager.get(Integer.parseInt(id));
			if (reservation == null)
				return Response.ok(
								new JSONObject().put("status", "reservation not found").toString())
						.status(404).build();
			return Response.ok(reservation).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {

		try {
			int t = Integer.parseInt(id);
			reservationManager.delete(t);
			return Response.ok(new JSONObject().put("status", "deletion succesful").toString()).build();
		} catch (Exception e) {
			return Response.ok(e).status(500).build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAll() {
		reservationManager.getMap().clear();
		productManager.getMap().clear();
		customerManager.getMap().clear();
		return Response.ok(
						new JSONObject().put("status", "Successfull clearing").toString())
				.build();
	}

}
