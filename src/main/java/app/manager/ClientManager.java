package app.manager;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import app.model.Client;
import app.repo.InMemoryRepository;
import app.repo.Repository;

public class ClientManager {

	private Repository<Client> repo;

	public ClientManager() {
		repo = new InMemoryRepository<>();
	}

	public Client get(int id) throws Exception {
		return repo.get((c) -> id == c.get());
	}

	@PUT
	@Path("/client")
	@Produces(MediaType.TEXT_PLAIN)
	public String create (@QueryParam("name") String n,  @QueryParam("surname") String s) {
		if (n == null || s == null)
			return "Could not create user, missing parameters";
		return repo.insert(new Client(n,s)).toString();
	}

	@GET
	@Path("/client")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		return Response.ok(new Client("Imie", "Nazwisko")).build();
	}

	@DELETE
	@Path("/client")
	public Response delete(@QueryParam("id") int id) {
		try {
			this.repo.delete(this.get(id));
			return Response.ok("Success").build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

}
