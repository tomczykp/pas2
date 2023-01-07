package app.dto;

import app.model.Product;
import app.model.Reservation;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class ProductDTO {

	@XmlElement
	public int productID;
	@XmlElement
	public double price;
	@XmlElement
	public List<Integer> reservations;

	public ProductDTO (Product p) {
		price = p.getPrice();

		reservations = new ArrayList<>();
		for (Reservation r : p.getReservations())
			reservations.add(r.getReservationID());
		productID = p.getProductID();

	}

	public ProductDTO () {}

}
