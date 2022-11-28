package app.endpoints;

import app.dto.CustomerDTO;
import app.dto.ModeratorDTO;
import app.model.Customer;
import app.model.Moderator;
import app.model.Reservation;
import app.exceptions.NotFoundException;
import app.managers.ModeratorManager;
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

@Path("/moderator")
public class ModeratorEndpoint {

    @Inject
    public ModeratorManager moderatorManager;

    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers (@QueryParam("username") String name,
                                     @QueryParam("exact") String exact) {
        Map<Integer, CustomerDTO> data;
        if (Objects.equals(name, "") || name == null) {
            data = moderatorManager.getCustomerMap();
        } else if (exact == null || Objects.equals(exact, "")) {
            data = moderatorManager.getCustomers(
                    (Customer c) -> (c.getUsername().contains(name)) || name.contains(c.getUsername()));
        } else {
            data = moderatorManager.getCustomers((Customer c) -> Objects.equals(c.getUsername(), name));
        }
        return Response.ok(data).build();
    }

    @GET
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") String id) {
        try {
            Customer customer = moderatorManager.getCustomer(Integer.parseInt(id));
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
            Moderator moderator = moderatorManager.getModerator(Integer.parseInt(id));
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
            Customer customer = moderatorManager.getCustomer(Integer.parseInt(id));
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
            Customer customer = moderatorManager.getCustomer(Integer.parseInt(id));
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
            Customer customer = moderatorManager.getCustomer(Integer.parseInt(id));
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
    @Path("/create/moderator")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response putModerator (@NotNull Moderator newModerator) {
        try {
            ModeratorDTO moderator = moderatorManager.createModerator(newModerator);
            return Response.ok(moderator).build();
        } catch (Exception e) {
            return Response.ok(e).status(409).build();
        }
    }

    @PUT
    @Path("/create/customer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response putCustomer (@NotNull Customer newCustomer) {
        try {
            CustomerDTO customer = moderatorManager.createCustomer(newCustomer);
            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.ok(e).status(409).build();
        }
    }


    @PUT
    @Path("/update/customer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer (CustomerDTO newCustomer) {
        try {
            int t = newCustomer.customerID;
            Customer res = moderatorManager.modifyCustomer(t,
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
