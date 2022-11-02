package app.dto;

import app.model.Product;
import app.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

	public long getPrice () {
		return price;
	}

	public List<ReservationDTO> getReservations () {
		return reservations;
	}

	public long getId () {
		return id;
	}

	private final long id;
	private final long price;
	private final List<ReservationDTO> reservations;

	public ProductDTO(Product p) {
		price = p.getPrice();
		reservations = new ArrayList<>();
		id = p.getProductID();
		for(Reservation r: p.getReservations())
			reservations.add(new ReservationDTO(r));

	}
}
