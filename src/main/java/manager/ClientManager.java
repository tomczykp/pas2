package manager;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import model.Client;
import repo.InMemoryRepository;
import repo.Repository;

public class ClientManager {

	private Repository<Client> repo;

	public ClientManager() {
		repo = new InMemoryRepository<Client>();
	}

	public Client get(int id) throws Exception {
		return repo.get((c) -> id == c.get());
	}

	@GET
	@Path("/client/create")
	@Produces(MediaType.TEXT_PLAIN)
	public String create (@QueryParam("name") String n,  @QueryParam("surname") String s) {
		if (n == null || s == null)
			return "Could not create user, missing parameters";
		return repo.insert(new Client(n,s)).toString();
	}

	@GET
	@Path("/client")
	public String page() {
		return "Hello world!";
	}



}
