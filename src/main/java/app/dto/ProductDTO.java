package app.dto;

import app.model.Product;

public class ProductDTO {

	public long getPrice () {
		return price;
	}

	public int getReservations () {
		return reservations;
	}

	public long getProductID () {
		return productID;
	}

	private final long productID;
	private final long price;
	private final int reservations;

	public ProductDTO (Product p) {
		price = p.getPrice();
		reservations = p.getReservations().size();
		productID = p.getProductID();

	}

}
