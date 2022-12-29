package app.endpoints;

import app.exceptions.NotFoundException;
import app.managers.ProductManager;
import app.managers.ReservationManager;
import app.managers.UserManager;
import app.model.Reservation;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;

@Path("/reservation")
public class ReservationEndpoint {

	@Inject
	private ReservationManager reservationManager;
	@Inject
	private UserManager userManager;
	@Inject
	private ProductManager productManager;

	@GET
	@RolesAllowed({"MODERATOR", "ADMINISTRATOR"})
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll () {
		return Response.ok(reservationManager.getMap().values()).build();
	}

	@GET
	@RolesAllowed({"CUSTOMER", "MODERATOR", "ADMINISTRATOR"})
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
	@RolesAllowed({"CUSTOMER"})
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
	@RolesAllowed({"CUSTOMER", "MODERATOR", "ADMINISTRATOR"})
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
	@RolesAllowed({"ADMINISTRATOR"})
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
	@RolesAllowed({"MODERATOR", "ADMINISTRATOR"})
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update (@NotNull Reservation newReservation) {
		try {
			int t = newReservation.getReservationID();
			Reservation res = reservationManager.modify(t,
					(Reservation current) -> current
							.switchCustomer(userManager.getCustomer(newReservation.getCustomer()), reservationManager.customerRepository)
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
	@RolesAllowed({"CUSTOMER", "ADMINISTRATOR"})
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
	@RolesAllowed({"CUSTOMER", "MODERATOR", "ADMINISTRATOR"})
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
