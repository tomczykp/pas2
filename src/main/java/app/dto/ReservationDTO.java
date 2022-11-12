package app.dto;

import app.model.Reservation;

import java.time.LocalDate;

public class ReservationDTO {

	public ProductDTO getProduct () {
		return product;
	}


	public CustomerDTO getCustomer () {
		return customer;
	}

	public LocalDate getEndDate () {
		return endDate;
	}

	public LocalDate getStartDate () {
		return startDate;
	}

	private final ProductDTO product;
	private final CustomerDTO customer;
	private LocalDate endDate;
	private LocalDate startDate;
	private final long reservationID;

	public long getReservationID () {
		return reservationID;
	}

	public ReservationDTO (Reservation r) {
		product = new ProductDTO(r.getProduct());
		customer = new CustomerDTO(r.getCustomer());
		endDate = r.getEndDate();
		startDate = r.getStartDate();
		reservationID = r.getReservationID();
	}

}
