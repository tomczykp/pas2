package app.endpoints;

import app.dto.ReservationDTO;
import app.exceptions.DateException;
import app.exceptions.NotFoundException;
import app.managers.CustomerManager;
import app.managers.ProductManager;
import app.managers.ReservationManager;
import app.model.Customer;
import app.model.Product;
import app.model.Reservation;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import javax.ejb.Stateless;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		return Response.ok(reservationManager.getMap().values()).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Reservation reservation = reservationManager.get(Integer.parseInt(id));
			return Response.ok(new ReservationDTO(reservation)).build();
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
			ReservationDTO reservation = reservationManager.create(r);
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
			reservationManager.delete(t);
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

	@PATCH
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update (ReservationDTO newReservation) {
		try {

			int t = newReservation.reservationID;
			Reservation res = reservationManager.modify(t,
					(Reservation current) -> current
							.switchCustomer((Customer) customerManager.get(newReservation.customerID))
							.setEndDate(newReservation.endDate)
							.setStartDate(newReservation.startDate)
							.switchProduct(productManager.get(newReservation.productID)));

			return Response.ok(new ReservationDTO(res)).build();
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
