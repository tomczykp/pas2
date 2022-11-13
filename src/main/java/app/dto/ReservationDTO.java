package app.dto;

import app.model.Reservation;

import java.time.LocalDate;

public class ReservationDTO {

	public int getProduct () {
		return pid;
	}


	public int getCustomer () {
		return cid;
	}

	public LocalDate getEndDate () {
		return endDate;
	}

	public LocalDate getStartDate () {
		return startDate;
	}

	private final int pid;
	private final int cid;
	private final LocalDate endDate;
	private final LocalDate startDate;
	private final int reservationID;

	public int getReservationID () {
		return reservationID;
	}

	public ReservationDTO (Reservation r) {
		pid = r.getProduct().getProductID();
		cid = r.getCustomer().getCustomerID();
		endDate = r.getEndDate();
		startDate = r.getStartDate();
		reservationID = r.getReservationID();
	}

}
