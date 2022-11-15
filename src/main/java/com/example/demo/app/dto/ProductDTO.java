package com.example.demo.app.dto;

import com.example.demo.app.model.Product;
import com.example.demo.app.model.Reservation;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {


	public int productID;

	public double price;

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
