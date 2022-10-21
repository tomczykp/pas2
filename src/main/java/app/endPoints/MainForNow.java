package app.endPoints;

import app.managers.AccountManager;
import app.model.Address;
import app.model.Client;
import app.model.Person;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import app.model.AccountType;

@Path("/main")
public class MainForNow {

    private static AccountManager manager;

    public MainForNow() {
        manager = new AccountManager();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String put(@QueryParam("name") String n, @QueryParam("surname") String s) {
        if (n == null || s == null) {
            return "Could not create user, missing parameters";
        }
        Address adress = new Address("Poland", "Lodz", "al.Politechniki", "23");
        Client client = new Person(20000, "+48 533998311", adress, "Mateusz", "Sochacki", "236652");
        return manager.createAccount(12.5, AccountType.Normal, client).toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") String id) {
        if (id == "" || id == null) {
            return Response.ok("Nothing here yet").build();
        }
        return Response.ok(manager.getMap().toString()).build();
    }

    @DELETE
    public Response delete(@QueryParam("id") String id) {
        manager.deleteAccount(id);
        return Response.ok("Success").build();
    }
}