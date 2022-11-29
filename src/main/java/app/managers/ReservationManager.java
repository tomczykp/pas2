package app.managers;

import app.FunctionThrows;
import app.exceptions.NotFoundException;
import app.model.Customer;
import app.model.Product;
import app.model.Reservation;
import app.repositories.CustomerRepository;
import app.repositories.ProductRepository;
import app.repositories.ReservationRepository;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ReservationManager {

	@Inject
	public ReservationRepository reservationRepository;

	@Inject
	public CustomerRepository customerRepository;

	@Inject
	public ProductRepository productRepository;

	synchronized public Reservation create (Reservation r) throws Exception {
		if (r.getStartDate().isBefore(LocalDate.now()) || r.getEndDate().isBefore(LocalDate.now()))
			throw new Exception("cannot make reservation in the past");

		if (r.getEndDate().isBefore(r.getStartDate()))
			throw new Exception("end date cannot be before start date");

		if (!customerRepository.get(r.getCustomer()).isActive())
			throw new Exception("customer is inactive");

		if (productRepository.get(r.getProduct()).isReserved(r.getStartDate(), r.getEndDate()))
			throw new Exception("product is already reserved");

		customerRepository.get(r.getCustomer()).getReservations().add(r);
		productRepository.get(r.getProduct()).getReservations().add(r);
		return reservationRepository.insert(r);
	}

	public void delete (int id, boolean force) throws Exception {
		Reservation reservation = reservationRepository.get(id);
		if (!force) {
			if ((reservation.getStartDate().isBefore(LocalDate.now()) || (reservation.getStartDate().isEqual(LocalDate.now())) && reservation.getEndDate().isAfter(LocalDate.now())))
				throw new Exception("cannot remove already started reservation");
		}
		customerRepository.get(reservation.getCustomer()).getReservations().remove(reservation);
		productRepository.get(reservation.getProduct()).getReservations().remove(reservation);
		reservationRepository.delete(id);
	}

	public Reservation modify (int id, FunctionThrows<Reservation> func) throws Exception {
		return reservationRepository.modify(id, func);
	}

	public Reservation get (int id) throws NotFoundException {
		return reservationRepository.get(id);
	}

	public Map<Integer, Reservation> get (Predicate<Reservation> predicate) {
		try {
			HashMap<Integer, Reservation> res = new HashMap<>();
			for (Map.Entry<Integer, Reservation> entry : reservationRepository.get(predicate).entrySet())
				res.put(entry.getKey(), entry.getValue());
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Reservation> getMap () {

		HashMap<Integer, Reservation> res = new HashMap<>();
		for (Map.Entry<Integer, Reservation> entry : reservationRepository.getMap().entrySet())
			res.put(entry.getKey(), entry.getValue());
		return res;
	}

	public List<Reservation> getCustomerReservations(int id) throws Exception{
		return customerRepository.get(id).getReservations();
	}

	public List<Reservation> getProductReservations(int id) throws Exception {
		return productRepository.get(id).getReservations();
	}
}
