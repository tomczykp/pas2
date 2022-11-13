package app.dto;

import app.model.Reservation;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

@XmlRootElement
public class ReservationDTO {

	@XmlElement
	public final int productID;
	@XmlElement
	public final int customerID;
	@XmlElement
	public final LocalDate endDate;
	@XmlElement
	public final LocalDate startDate;
	@XmlElement
	public final int reservationID;

	public int getReservationID () {
		return reservationID;
	}

	public ReservationDTO (Reservation r) {
		productID = r.getProduct().getProductID();
		customerID = r.getCustomer().getCustomerID();
		endDate = r.getEndDate();
		startDate = r.getStartDate();
		reservationID = r.getReservationID();
	}

}
