package app.endpoints.a;

import app.managers.a.CustomerManager;
import app.managers.a.ProductManager;
import app.managers.a.ReservationManager;
import app.model.a.Product;
import app.model.a.Reservation;
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
        this.reservationManager = new ReservationManager();
        this.customerManager = new CustomerManager();
        this.productManager = new ProductManager();
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
            int clientId = Integer.valueOf(cid);
            int productId = Integer.valueOf(pid);
            Reservation reservation = new Reservation(endDate, this.customerManager.get(clientId), this.productManager.get(productId));
            return Response.ok(reservation).build();
        } catch(NumberFormatException e) {
            return Response.ok(e).status(500).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(this.reservationManager.getMap()).build();
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
