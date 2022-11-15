package com.example.demo.app.dto;

import com.example.demo.app.model.Reservation;
import java.time.LocalDate;


public class ReservationDTO {

	public int productID;

	public int customerID;

	public LocalDate endDate;

	public LocalDate startDate;

	public int reservationID;

	public ReservationDTO (Reservation r) {
		productID = r.getProduct().getProductID();
		customerID = r.getCustomer().getCustomerID();
		endDate = r.getEndDate();
		startDate = r.getStartDate();
		reservationID = r.getReservationID();
	}

	public ReservationDTO () {}

}
