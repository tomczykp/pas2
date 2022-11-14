package app.dto;

import app.model.Reservation;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

@XmlRootElement
public class ReservationDTO {

	@XmlElement
	public int productID;
	@XmlElement
	public int customerID;
	@XmlElement
	public LocalDate endDate;
	@XmlElement
	public LocalDate startDate;
	@XmlElement
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
