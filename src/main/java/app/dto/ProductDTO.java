package app.dto;

import app.model.Product;
import app.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

	public double getPrice () {
		return price;
	}

	public List<Integer> getReservations () {
		return reservations;
	}

	public int getProductID () {
		return productID;
	}

	private final int productID;
	private final double price;
	private final List<Integer> reservations;

	public ProductDTO (Product p) {
		price = p.getPrice();

		reservations = new ArrayList<>();
		for (Reservation r : p.getReservations())
			reservations.add(r.getReservationID());
		productID = p.getProductID();

	}

}
