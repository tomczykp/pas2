package app.endpoints;

import app.dto.ProductDTO;
import app.dto.ReservationDTO;
import app.exceptions.NotFoundException;
import app.managers.ProductManager;
import app.model.Product;
import app.model.Reservation;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("/product")
public class ProductEndpoint {

	@Inject
	private ProductManager manager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll () {
		return Response.ok(manager.getMap()).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Product product = manager.get(Integer.parseInt(id));
			return Response.ok(new ProductDTO(product)).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		}
	}

	private List<ReservationDTO> mapDTO (List<Reservation> reservations) {
		List<ReservationDTO> res = new ArrayList<>();
		for (Reservation r : reservations)
			res.add(new ReservationDTO(r));
		return res;
	}

	@GET
	@Path("/{id}/reservations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReservations (@PathParam("id") String id, @QueryParam("past") boolean fromPast) {
		try {
			Product product = manager.get(Integer.parseInt(id));
			List<Reservation> res;
			if (fromPast)
				res = product.getPastReservations();
			else
				res = product.getFutureReservations();

			return Response.ok(mapDTO(res)).build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response put (@FormParam("price") String p) {
		if (Objects.equals(p, "") || p == null)
			return Response.ok(
							new JSONObject().put("status", "missing parameter 'price'").toString())
					.status(404).build();

		try {
			int t = Integer.parseInt(p);
			ProductDTO product = manager.create(t);
			return Response.ok(product).build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete (@PathParam("id") String id) {

		try {
			int t = Integer.parseInt(id);
			manager.delete(t);
			return Response.ok(new JSONObject().put("status", "deletion succesful").toString()).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(500).build();
		}
	}

	@PATCH
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update (@PathParam("id") String id, ProductDTO p) {
		try {

			int t = Integer.parseInt(id);
			Product res = manager.modify(t, (Product p1) -> p1.setPrice(p.getPrice()));

			return Response.ok(new ProductDTO(res)).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(500).build();
		}
	}

}
