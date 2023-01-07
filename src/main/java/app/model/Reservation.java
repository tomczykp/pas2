package app.model;

import app.exceptions.NotFoundException;
import app.repositories.ProductRepository;
import app.repositories.UserRepository;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.time.LocalDate;

@XmlRootElement
public class Reservation {
	@XmlElement
	private int reservationID;
	@XmlElement
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDate startDate;
	@XmlElement
	@XmlJavaTypeAdapter(DateAdapter.class)
	private LocalDate endDate;

	@XmlElement
	private Integer customer;

	@XmlElement
	private Integer product;


	public int getReservationID() {
		return reservationID;
	}
	public Integer getCustomer() {
		return customer;
	}

	public Integer getProduct() {
		return product;
	}

	public void setCustomer(Integer customer) {
		this.customer = customer;
	}

	public void setProduct(Integer product) {
		this.product = product;
	}


	public Reservation setId (int id) {
		reservationID = id;
		return this;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	public Reservation setStartDate (LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	public Reservation setEndDate (LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public Reservation switchCustomer (Customer nCustomer, UserRepository repository) throws NotFoundException {
		Customer current = (Customer) repository.get(this.getCustomer());
		current.getReservations().remove(this);
		nCustomer.addReservation(this);
		this.setCustomer(nCustomer.getUserID());
		return this;
	}

	private boolean overlapingReservations (Product p) {
		for (Reservation r : p.getFutureReservations())
			if (r.getProduct() != p.getProductID()) {
				if (r.startDate.isEqual(startDate) || r.endDate.isEqual(endDate)
						|| (r.startDate.isAfter(startDate) && r.startDate.isBefore(endDate))
						|| (r.endDate.isAfter(startDate) && r.endDate.isBefore(endDate))
						|| (r.startDate.isBefore(startDate) && r.endDate.isAfter(endDate)))
					return true;
			}
		return false;
	}

	public Reservation switchProduct (Product p, ProductRepository repository) throws Exception {

		if (overlapingReservations(p))
			throw new Exception("cannot switch product, already reserved at this period");

		Product curr = repository.get(this.getProduct());
		curr.getReservations().remove(this);
		p.getReservations().add(this);
		this.setProduct(p.getProductID());
		return this;
	}

	public Reservation (LocalDate startDate, LocalDate endDate, Integer customer, Integer product) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.customer = customer;
		this.product = product;
	}

	public Reservation () {}

	@Override
	public int hashCode () {
		return new HashCodeBuilder(17, 37)
				.append(reservationID)
				.append(startDate)
				.append(endDate)
				.append(customer)
				.append(product).toHashCode();
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;

		if (o.getClass() != getClass()) return false;
		Reservation that = (Reservation) o;

		return new EqualsBuilder()
				.append(reservationID, that.reservationID)
				.append(startDate, that.startDate)
				.append(endDate, that.endDate)
				.append(customer, that.customer)
				.append(product, that.product).isEquals();
	}

	public LocalDate getStartDate () {
		return startDate;
	}

	public LocalDate getEndDate () {
		return endDate;
	}

}