package app.dto;

import app.managers.CustomerManager;
import app.model.Reservation;

import java.time.LocalDateTime;

public class ReservationDTO {

	public ProductDTO getProduct () {
		return product;
	}


	public CustomerDTO getCustomer () {
		return customer;
	}

	public LocalDateTime getEndDate () {
		return endDate;
	}

	public LocalDateTime getStartDate () {
		return startDate;
	}

	private ProductDTO product;
	private CustomerDTO customer;
	private LocalDateTime endDate;
	private LocalDateTime startDate;
	private long id;

	public long getId () {
		return id;
	}

	public ReservationDTO (Reservation r) {
		product = new ProductDTO(r.getProduct());
		customer = new CustomerDTO(r.getCustomer());
		endDate = r.getEndDate();
		startDate = r.getStartDate();
		id = r.getReservationID();
	}
}
