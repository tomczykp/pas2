package app.endpoints;

import app.dto.AdministratorDTO;
import app.dto.CustomerDTO;
import app.dto.ModeratorDTO;
import app.model.Administrator;
import app.model.Customer;
import app.model.Moderator;
import app.model.Reservation;
import app.exceptions.NotFoundException;
import app.managers.AdministratorManager;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Path("/administrator")
public class AdministratorEndpoint {

    @Inject
    private AdministratorManager administratorManager;


    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers (@QueryParam("username") String name,
                                     @QueryParam("exact") String exact) {
        Map<Integer, CustomerDTO> data;
        if (Objects.equals(name, "") || name == null) {
            data = administratorManager.getCustomerMap();
        } else if (exact == null || Objects.equals(exact, "")) {
            data = administratorManager.getCustomers(
                    (Customer c) -> (c.getUsername().contains(name)) || name.contains(c.getUsername()));
        } else {
            data = administratorManager.getCustomers((Customer c) -> Objects.equals(c.getUsername(), name));
        }
        return Response.ok(data.values()).build();
    }

    @GET
    @Path("/moderators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllModerators (@QueryParam("username") String name,
                                     @QueryParam("exact") String exact) {
        Map<Integer, ModeratorDTO> data;
        if (Objects.equals(name, "") || name == null) {
            data = administratorManager.getModeratorMap();
        } else if (exact == null || Objects.equals(exact, "")) {
            data = administratorManager.getModerators(
                    (Moderator c) -> (c.getUsername().contains(name)) || name.contains(c.getUsername()));
        } else {
            data = administratorManager.getModerators((Moderator c) -> Objects.equals(c.getUsername(), name));
        }
        return Response.ok(data.values()).build();
    }

    @GET
    @Path("/administrators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAdministrators (@QueryParam("username") String name,
                                          @QueryParam("exact") String exact) {
        Map<Integer, AdministratorDTO> data;
        if (Objects.equals(name, "") || name == null) {
            data = administratorManager.getAdministratorMap();
        } else if (exact == null || Objects.equals(exact, "")) {
            data = administratorManager.getAdministrators(
                    (Administrator c) -> (c.getUsername().contains(name)) || name.contains(c.getUsername()));
        } else {
            data = administratorManager.getAdministrators((Administrator c) -> Objects.equals(c.getUsername(), name));
        }
        return Response.ok(data.values()).build();
    }

    @GET
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") String id) {
        try {
            Customer customer = administratorManager.getCustomer(Integer.parseInt(id));
            return Response.ok(new CustomerDTO(customer)).build();
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

    @GET
    @Path("/moderator/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModerator(@PathParam("id") String id) {
        try {
            Moderator moderator = administratorManager.getModerator(Integer.parseInt(id));
            return Response.ok(new ModeratorDTO(moderator)).build();
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

    @GET
    @Path("/administrator/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdministrator(@PathParam("id") String id) {
        try {
            Administrator administrator = administratorManager.getAdministrator(Integer.parseInt(id));
            return Response.ok(new AdministratorDTO(administrator)).build();
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

    private List<Reservation> mapDTO (List<Reservation> reservations) {
        List<Reservation> res = new ArrayList<>();
        for (Reservation r : reservations)
            res.add(r);
        return res;
    }

    @GET
    @Path("/{id}/reservations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservations (@PathParam("id") String id, @QueryParam("past") boolean fromPast) {
        try {
            Customer customer = administratorManager.getCustomer(Integer.parseInt(id));
            List<Reservation> res;
            if (fromPast)
                res = customer.getPastReservations();
            else
                res = customer.getFutureReservations();

            return Response.ok(mapDTO(res)).build();
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

    @PUT
    @Path("/{id}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setActive (@PathParam("id") String id) {
        try {
            Customer customer = administratorManager.getCustomer(Integer.parseInt(id));
            customer.setActive(true);
            return Response.ok(
                    new JSONObject().put("status", "set to active")
                            .toString()).status(200).build();
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

    @PUT
    @Path("/{id}/deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setDeactivate (@PathParam("id") String id) {
        try {
            Customer customer = administratorManager.getCustomer(Integer.parseInt(id));
            if (!customer.getFutureReservations().isEmpty())
                throw new Exception("cannot deactivate user with ongoing reservations");

            customer.setActive(false);
            return Response.ok(
                    new JSONObject().put("status", "set to deactive")
                            .toString()).status(200).build();

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

    @PUT
    @Path("/create/administrator")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAdministrator (@NotNull Administrator newAdministrator) {
        try {
            AdministratorDTO admin = administratorManager.createAdministrator(newAdministrator);
            return Response.ok(admin).build();
        } catch (Exception e) {
            return Response.ok(e).status(409).build();
        }
    }

    @PUT
    @Path("/create/moderator")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putModerator (@NotNull Moderator newModerator) {
        try {
            ModeratorDTO moderator = administratorManager.createModerator(newModerator);
            return Response.ok(moderator).build();
        } catch (Exception e) {
            return Response.ok(e).status(409).build();
        }
    }

    @PUT
    @Path("/create/customer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putCustomer (@NotNull Customer newCustomer) {
        try {
            CustomerDTO customer = administratorManager.createCustomer(newCustomer);
            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.ok(e).status(409).build();
        }
    }


    @PUT
    @Path("/update/customer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer (@NotNull CustomerDTO newCustomer) {
        try {

            int t = newCustomer.customerID;
            Customer res = administratorManager.modifyCustomer(t,
                    (Customer current) -> current
                            .setActive(newCustomer.active)
                            .setEmail(newCustomer.email));

            return Response.ok(new CustomerDTO(res)).build();
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
