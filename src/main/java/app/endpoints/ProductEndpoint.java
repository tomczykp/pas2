package app.endpoints;

import app.dto.ProductDTO;
import app.managers.ProductManager;
import app.model.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

@Path("/product")
public class ProductEndpoint {

	@Inject
	private ProductManager manager;

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response put(@FormParam("price") String p) {
		if (Objects.equals(p, "") || p == null)
			return Response.ok(
					new JSONObject().put("status", "missing parameter price").toString())
					.status(404).build();

		try {
			int t = Integer.parseInt(p);
			Product product = manager.create(t);
			return Response.ok(new ProductDTO(product)).build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get (@PathParam("id") String id) {
		try {
			Product product = manager.get(Integer.parseInt(id));
			if (product == null)
				return Response.ok(new JSONObject().put("status", "product not found").toString()).status(404).build();
			return Response.ok(product).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@QueryParam("exact") String exact) {
		Map<Integer, Product> data = manager.getMap();
		if (exact == null || exact.equals(""))
			return Response.ok(data).build();
		return Response.ok(data).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {

		try {
			int t = Integer.parseInt(id);
			manager.delete(t);
			return Response.ok(
					new JSONObject().put("status", "deletion succesful").toString())
					.build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		} catch (Exception e) {
			return Response.ok(e.getMessage()).status(500).build();
		}
	}
}
