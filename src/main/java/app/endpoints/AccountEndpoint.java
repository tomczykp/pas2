package app.endpoints;

import app.managers.AccountManager;
import app.model.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;

@Path("/account")
public class AccountEndpoint {
        /*
    @Inject
    private AccountManager manager;

    public AccountEndpoint () {
        manager = new AccountManager();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@QueryParam("name") String n, @QueryParam("surname") String s) {
        if (Objects.equals(n, "") || Objects.equals(s, "")
                || s == null || n == null)
            return Response.ok("Could not create user, missing parameters").build();

        Address adress = new Address("Poland", "Lodz", "al.Politechniki", "23");
        Client client = new Person(20000, "+48 533998311", adress, n, s, "236652");
        try {
            return Response.ok(manager.createAccount(12.5, AccountType.Normal, client)).build();
        } catch (Exception e) {
            return Response.ok(e.getStackTrace()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") String id) {
        if (Objects.equals(id, "") || id == null)
            return Response.ok(manager.getMap()).build();

        Account account = manager.get(id);
        if (account.equals(null))
            return Response.ok("{'status': 'Account not found'}").build();
        return Response.ok( account + ": hello : " + id).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("id") String id) {
        manager.deleteAccount(id);
        return Response.ok("Success").build();
    }

         */
}