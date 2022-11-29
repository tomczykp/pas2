package app.endpoints;

import app.exceptions.NotFoundException;
import app.managers.CustomerManager;
import app.managers.ProductManager;
import app.managers.ReservationManager;
import app.model.Reservation;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.List;

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
		return Response.ok(reservationManager.getMap().values()).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Reservation reservation = reservationManager.get(Integer.parseInt(id));
			return Response.ok(reservation).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "reservation not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put (@NotNull Reservation r) {
		try {
			Reservation reservation = reservationManager.create(r);
			return Response.ok(reservation).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete (@PathParam("id") String id) {

		try {
			int t = Integer.parseInt(id);
			reservationManager.delete(t, false);
			return Response.ok(new JSONObject().put("status", "deletion succesful").toString()).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "reservation not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	@DELETE
	@Path("/forced/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteF (@PathParam("id") String id) {

		try {
			int t = Integer.parseInt(id);
			reservationManager.delete(t, true);
			return Response.ok(new JSONObject().put("status", "deletion succesful").toString()).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "reservation not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update (@NotNull Reservation newReservation) {
		try {
			int t = newReservation.getReservationID();
			Reservation res = reservationManager.modify(t,
					(Reservation current) -> current
							.switchCustomer(customerManager.get(newReservation.getCustomer()), reservationManager.customerRepository)
							.setEndDate(newReservation.getEndDate())
							.setStartDate(newReservation.getStartDate())
							.switchProduct(productManager.get(newReservation.getProduct()), reservationManager.productRepository));
			return Response.ok(res).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	@GET
	@Path("/client/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@PathParam("id") String id) {
		try {
			int t = Integer.parseInt(id);
			return Response.ok(reservationManager.getCustomerReservations(t)).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "reservation not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	@GET
	@Path("/product/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct(@PathParam("id") String id) {
		try {
			int t = Integer.parseInt(id);
			return Response.ok(reservationManager.getProductReservations(t)).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "reservation not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}
}
