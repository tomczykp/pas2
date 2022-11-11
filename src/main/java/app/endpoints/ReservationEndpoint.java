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
			return Response.ok(reservation).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}  catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", "reservation not found")
							.toString()).status(404).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response put (
						@FormParam("sdate") String start,
						@FormParam("edate") String end,
						@FormParam("cid") String cid,
						@FormParam("pid") String pid) {

		if ( Objects.equals(start, "") || start == null)
			return Response.ok(
							new JSONObject().put("status", "missing arguments 'sdate'").toString())
					.status(404).build();
		if ( Objects.equals(end, "") || end == null)
			return Response.ok(
							new JSONObject().put("status", "missing arguments 'edate'").toString())
					.status(404).build();
		if ( Objects.equals(cid, "") || cid == null)
			return Response.ok(
							new JSONObject().put("status", "missing arguments 'cid'").toString())
					.status(404).build();
		if ( Objects.equals(pid, "") || pid == null)
			return Response.ok(
							new JSONObject().put("status", "missing arguments 'pid'").toString())
					.status(404).build();

		try {
			LocalDateTime endDate = LocalDateTime.parse(end);
			LocalDateTime startDate = LocalDateTime.parse(start);
			Customer customer = customerManager.get(Integer.parseInt(cid));
			Product product = productManager.get(Integer.parseInt(pid));
			ReservationDTO reservation = reservationManager.create(startDate, endDate, customer, product);

			return Response.ok(reservation).build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getClass())
							.toString()).status(500).build();
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
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(500).build();
		}
	}

}
