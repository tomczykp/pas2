package app.endpoints;

import app.auth.JwsProvider;
import app.dto.AdministratorDTO;
import app.dto.ChangePasswordDTO;
import app.dto.CustomerDTO;
import app.dto.ModeratorDTO;
import app.exceptions.InvalidJWSException;
import app.exceptions.NotFoundException;
import app.exceptions.UnmachingPasswordsException;
import app.managers.UserManager;
import app.model.*;
import com.nimbusds.jose.JOSEException;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Path("/")
public class UserEndpoint {

    @Inject
    private UserManager userManager;

    @GET
    @RolesAllowed({"ADMINISTRATOR", "CUSTOMER"})
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers (@QueryParam("username") String name,
                                     @QueryParam("exact") String exact){
        Map<Integer, CustomerDTO> data;
        if (Objects.equals(name, "") || name == null) {
            data = userManager.getCustomerMap();
        } else if (exact == null || Objects.equals(exact, "")) {
            data = userManager.getCustomers(
                    (User c) -> (c.getUsername().contains(name)));
        } else {
            data = userManager.getCustomers((User c) -> Objects.equals(c.getUsername(), name));
        }
        return Response.ok(data.values()).build();
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    @Path("/moderators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllModerators (@QueryParam("username") String name,
                                      @QueryParam("exact") String exact) {
        Map<Integer, ModeratorDTO> data;
        if (Objects.equals(name, "") || name == null) {
            data = userManager.getModeratorMap();
        } else if (exact == null || Objects.equals(exact, "")) {
            data = userManager.getModerators(
                    (User c) -> (c.getUsername().contains(name)) || name.contains(c.getUsername()));
        } else {
            data = userManager.getModerators((User c) -> Objects.equals(c.getUsername(), name));
        }
        return Response.ok(data.values()).build();
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR"})
    @Path("/administrators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAdministrators (@QueryParam("username") String name,
                                          @QueryParam("exact") String exact) {
        Map<Integer, AdministratorDTO> data;
        if (Objects.equals(name, "") || name == null) {
            data = userManager.getAdministratorMap();
        } else if (exact == null || Objects.equals(exact, "")) {
            data = userManager.getAdministrators(
                    (User c) -> (c.getUsername().contains(name)) || name.contains(c.getUsername()));
        } else {
            data = userManager.getAdministrators((User c) -> Objects.equals(c.getUsername(), name));
        }
        return Response.ok(data.values()).build();
    }

    @GET
    @RolesAllowed({"CUSTOMER", "ADMINISTRATOR"})
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("id") int id) {
        try {
            Customer customer = userManager.getCustomer(id);
            String payload = userManager.getJwsFromUser(id);
            return Response.ok(new CustomerDTO(customer)).header("ETag", payload).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (app.exceptions.NotFoundException e) {
            return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

    @GET
    @RolesAllowed({"MODERATOR", "ADMINISTRATOR"})
    @Path("/moderator/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModerator(@PathParam("id") int id) {
        try {
            Moderator moderator = userManager.getModerator(id);
            String payload = userManager.getJwsFromUser(id);
            return Response.ok(new ModeratorDTO(moderator)).header("ETag", payload).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (app.exceptions.NotFoundException e) {
            return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

    @GET
    @RolesAllowed({"ADMINISTRATOR"})
    @Path("/administrator/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdministrator(@PathParam("id") String id) {
        try {
            Administrator administrator = userManager.getAdministrator(Integer.parseInt(id));
            return Response.ok(new AdministratorDTO(administrator)).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (app.exceptions.NotFoundException e) {
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
    @RolesAllowed({"CUSTOMER", "ADMINISTRATOR"})
    @Path("/customer/reservations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservations (@QueryParam("id") Integer id, @QueryParam("past") boolean fromPast) {
        try {
            int customerID;
            if (id == null) {
                customerID = this.userManager.getUserFromServerContext().getUserID();
            } else {
                customerID = id;
            }
            Customer customer = userManager.getCustomer(customerID);
            List<Reservation> res;
            if (fromPast)
                res = customer.getPastReservations();
            else
                res = customer.getFutureReservations();

            return Response.ok(mapDTO(res)).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (app.exceptions.NotFoundException e) {
            return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMINISTRATOR"})
    @Path("/customer/{id}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setActive (@PathParam("id") String id) {
        try {
            Customer customer = userManager.getCustomer(Integer.parseInt(id));
            customer.setActive(true);
            return Response.ok(
                    new JSONObject().put("status", "set to active")
                            .toString()).status(200).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (app.exceptions.NotFoundException e) {
            return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMINISTRATOR"})
    @Path("/customer/{id}/deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setDeactivate (@PathParam("id") String id) {
        try {
            Customer customer = userManager.getCustomer(Integer.parseInt(id));
            if (!customer.getFutureReservations().isEmpty())
                throw new Exception("cannot deactivate user with ongoing reservations");

            customer.setActive(false);
            return Response.ok(
                    new JSONObject().put("status", "set to deactive")
                            .toString()).status(200).build();

        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (app.exceptions.NotFoundException e) {
            return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMINISTRATOR"})
    @Path("/administrator/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAdministrator (@NotNull Administrator newAdministrator) {
        try {
            AdministratorDTO admin = userManager.createAdministrator(newAdministrator);
            return Response.ok(admin).build();
        } catch (Exception e) {
            return Response.ok(e).status(409).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMINISTRATOR"})
    @Path("/moderator/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putModerator (@NotNull Moderator newModerator) {
        try {
            ModeratorDTO moderator = userManager.createModerator(newModerator);
            return Response.ok(moderator).build();
        } catch (Exception e) {
            return Response.ok(e).status(409).build();
        }
    }

    @PUT
    @RolesAllowed({"ANONYMOUS"})
    @Path("/customer/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putCustomer (@NotNull Customer newCustomer) {
        try {
            CustomerDTO customer = userManager.createCustomer(newCustomer);
            return Response.ok(customer).build();
        } catch (Exception e) {
            return Response.ok(e).status(400).build();
        }
    }

    @DELETE
    @RolesAllowed({"CUSTOMSER", "ADMINISTRATOR"})
    @Path("/user/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete (@PathParam("id") String id) {

        try {
            int t = Integer.parseInt(id);
            userManager.deleteUser(t);
            return Response.ok(new JSONObject().put("status", "deletion succesful").toString()).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

    @PUT
    @RolesAllowed({"CUSTOMER", "ADMINISTRATOR"})
    @Path("/customer/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer (@NotNull CustomerDTO newCustomer, @Context HttpServletRequest request) {
        try {
            String jws = request.getHeader("If-Match");
            if (jws == null) {
                return Response.status(400).build();
            }
            int t = newCustomer.customerID;
            userManager.modifyCustomer(t,
                    (Customer current) -> current
                            .setActive(newCustomer.active)
                            .setEmail(newCustomer.email), jws, newCustomer);
            return Response.ok().build();
        } catch (InvalidJWSException e) {
            return Response.status(400).build();
        } catch (NumberFormatException e) {
            return Response.status(406).build();
        } catch (app.exceptions.NotFoundException e) {
            return Response.ok(new JSONObject().put("status", "customer not found").toString()).status(404).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

    @PUT
    @RolesAllowed({"MODERATOR", "ADMINISTRATOR", "CUSTOMER"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/passwordChange")
    public Response changePassword(@NotNull ChangePasswordDTO changePasswordDTO) {
        try {
            this.userManager.changePassword(changePasswordDTO.oldPassword, changePasswordDTO.newPassword);
            return Response.ok().build();
        } catch (UnmachingPasswordsException e) {
            return Response.status(400).build();
        }
    }

    @PUT
    @RolesAllowed({"MODERATOR", "ADMINISTRATOR"})
    @Path("/moderator/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateModerator (@NotNull ModeratorDTO newModerator, @Context HttpServletRequest request) {
        String jws = request.getHeader("If-Match");
        if (jws == null) {
            return Response.status(400).build();
        }
        try {
            int t = newModerator.moderatorID;
            userManager.modifyModerator(t,
                    (Moderator current) -> current
                            .setEmail(newModerator.email), jws, newModerator);

            return Response.ok().build();
        } catch (InvalidJWSException e) {
            return Response.status(400).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(406).build();
        } catch (NotFoundException e) {
            return Response.ok(new JSONObject().put("status", "moderator not found").toString()).status(404).build();
        } catch (Exception e) {
            return Response.ok(
                    new JSONObject().put("status", e.getMessage())
                            .toString()).status(409).build();
        }
    }

//    @PUT
//    @Path("/administrator/update")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response updateAdministrator (@NotNull AdministratorDTO newAdministrator) {
//        try {
//            int t = newAdministrator.administratorID;
//            userManager.modifyAdministrator(t,
//                    (Administrator current) -> current
//                            .setUsername(newAdministrator.username));
//            return Response.ok().build();
//        } catch (NumberFormatException e) {
//            return Response.ok(e).status(406).build();
//        } catch (NotFoundException e) {
//            return Response.ok(new JSONObject().put("status", "administrator not found").toString()).status(404).build();
//        } catch (Exception e) {
//            return Response.ok(
//                    new JSONObject().put("status", e.getMessage())
//                            .toString()).status(409).build();
//        }
//    }
}
