package app.endpoints;

import app.managers.ProductManager;
import app.model.*;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.Objects;

@Path("/product")
public class ProductEndpoint {
	
	@Inject
	private ProductManager manager;

	public ProductEndpoint () {
		this.manager = new ProductManager();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(@QueryParam("price") String p) {
		if (Objects.equals(p, "") || p == null)
			return Response.ok("{'status':'missing argument `price`'}")
					.status(404).build();

		try {
			int t = Integer.parseInt(p);
			Product product = manager.create(t);
			return Response.ok(product).build();

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
				return Response.ok("{\"status\":\"Product not found\"}").status(404).build();
			return Response.ok(product).build();
		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoot() {
		return Response.ok(this.manager.getMap()).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {
		if (Objects.equals(id, "") || id == null)
			return Response.ok("{\"status\":\"missing argument `price`\"}")
					.status(404).build();

		try {
			int t = Integer.parseInt(id);
			manager.delete(t);
			return Response.ok("{\"status\":\"Success\"}").build();

		} catch (NumberFormatException e) {
			return Response.ok(e).status(500).build();
		}
	}

//	@Produces(MediaType.APPLICATION_JSON)
//	public Response update(@QueryParam("price") String p) {
//		if (Objects.equals(p, "") || p == null)
//			return Response.ok("{'status':'missing argument `price`'}")
//					.status(404).build();
//
//		try {
//			int t = Integer.parseInt(p);
//			Product product = manager.create(t);
//			return Response.ok(product).build();
//
//		} catch (NumberFormatException e) {
//			return Response.ok(e).status(500).build();
//		}
//	}


}
