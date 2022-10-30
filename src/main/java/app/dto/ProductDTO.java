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

	private long id;
	private long price;
	private List<ReservationDTO> reservations;

	public ProductDTO(Product p) {
		this.price = p.getPrice();
		this.reservations = new ArrayList<>();
		id = p.getId();
		for(Reservation r: p.getReservations())
			reservations.add(new ReservationDTO(r));

	}
}
