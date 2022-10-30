package app.endpoints;

import app.managers.CustomerManager;
import app.managers.ProductManager;
import app.managers.ReservationManager;
import app.model.Product;
import app.model.Reservation;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Objects;

@Path("/reservation")
public class ReservationEndpoint {

    private ReservationManager reservationManager;
    private CustomerManager customerManager;
    private ProductManager productManager;

    public ReservationEndpoint() {
        reservationManager = new ReservationManager();
        customerManager = new CustomerManager();
        productManager = new ProductManager();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@QueryParam("date") String d, @QueryParam("cid") String cid, @QueryParam("pid") String pid) {
        if (Objects.equals(d, "") || Objects.equals(cid, "") || Objects.equals(pid, "")) {
            return Response.ok("{'status':'missing argument `price`'}")
                    .status(404).build();
        }

        try {
            LocalDateTime endDate = LocalDateTime.parse(d);
			LocalDateTime startDate = LocalDateTime.now();
            int clientId = Integer.valueOf(cid);
            int productId = Integer.valueOf(pid);
            Reservation reservation = new Reservation(startDate, endDate, customerManager.get(clientId), productManager.get(productId));
            return Response.ok(reservation).build();
        } catch(NumberFormatException e) {
            return Response.ok(e).status(500).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(reservationManager.getMap()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get (@PathParam("id") String id) {
        try {
            Reservation reservation = reservationManager.get(Integer.valueOf(id));
            if (reservation == null)
                return Response.ok(new JSONObject().put("status", "Product not found").toString()).status(404).build();
            return Response.ok(reservation).build();
        } catch (NumberFormatException e) {
            return Response.ok(e).status(500).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        if (Objects.equals(id, "") || id == null) {
            return  Response.ok("{\"status\":\"missing argument `price`\"}").status(404).build();
        }

        try {
            int i = Integer.valueOf(id);
			reservationManager.delete(i);
            return Response.ok("{\"status\":\"Success\"}").build();
        } catch (Exception e) {
            return Response.ok(e).status(500).build();
        }
    }
}
