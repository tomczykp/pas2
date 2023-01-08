package app.endpoints;

import app.dto.ProductDTO;
import app.exceptions.InvalidJWSException;
import app.exceptions.NotFoundException;
import app.managers.ProductManager;
import app.model.Product;
import app.model.Reservation;
import com.nimbusds.jose.JOSEException;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("/product")
public class ProductEndpoint {

	@Inject
	private ProductManager manager;

	@GET
	@RolesAllowed({"CUSTOMER", "MODERATOR", "ADMINISTRATOR", "ANONYMOUS"})
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll () {
		return Response.ok(manager.getMap().values()).build();
	}

	@GET
	@RolesAllowed({"CUSTOMER", "MODERATOR", "ADMINISTRATOR"})
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") int id) {
		try {
			Product product = manager.get(id);
			String payload = this.manager.getJwsFromProduct(id);
			return Response.ok(new ProductDTO(product)).header("ETag", payload).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		}  catch (Exception e) {
			return Response.ok(new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	private List<Reservation> mapDTO (List<Reservation> reservations) {
		List<Reservation> res = new ArrayList<>();
		for (Reservation r : reservations)
			res.add(r);
		return res;
	}

	@GET
	@RolesAllowed({"MODERATOR", "ADMINISTRATOR"})
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
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		}
	}

	@PUT
	@RolesAllowed({"MODERATOR"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put (Product p) {
		if (Objects.equals(p, "") || p == null)
			return Response.ok(
							new JSONObject().put("status", "missing parameter 'price'").toString())
					.status(404).build();

		try {
			ProductDTO product = manager.create(p);
			return Response.ok(product).build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		}
	}

	@DELETE
	@RolesAllowed({"MODERATOR"})
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete (@PathParam("id") String id) {

		try {
			int t = Integer.parseInt(id);
			manager.delete(t);
			return Response.ok(new JSONObject().put("status", "deletion succesful").toString()).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

	@PUT
	@RolesAllowed({"MODERATOR"})
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update (@NotNull ProductDTO p, @Context HttpServletRequest request) {
		try {
			String jws = request.getHeader("If-Match");
			if (jws == null) {
				return Response.status(400).build();
			}
			int t = p.productID;
			Product res = manager.modify(t, (Product p1) -> p1.setPrice(p.price), jws, p);
			return Response.ok(new ProductDTO(res)).build();
		}catch (InvalidJWSException e) {
			return Response.status(400).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(406).build();
		} catch (NotFoundException e) {
			return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
		} catch (Exception e) {
			return Response.ok(
					new JSONObject().put("status", e.getMessage())
							.toString()).status(409).build();
		}
	}

}
