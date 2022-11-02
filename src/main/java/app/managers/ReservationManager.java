package app.managers;

import app.model.Customer;
import app.model.Product;
import app.model.Reservation;
import app.repositories.ReservationRepository;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReservationManager {

	@Inject
	private ReservationRepository reservationRepository;

	public Reservation create(LocalDateTime b, LocalDateTime e, Customer c, Product p) throws Exception {

		if (b.isBefore(LocalDateTime.now()) || e.isBefore(LocalDateTime.now()))
			throw new Exception("cannot make reservation in the past");

		if (e.isBefore(b))
			throw new Exception("End date cannot be before start date");

		if (!c.isActive() || !c.getCurrentReservations().isEmpty())
			throw new Exception("customer has active reservations or is inactive");

		if (p.getCurrentReservations().isEmpty())
			return reservationRepository.insert(new Reservation(b, e, c, p));

		throw new Exception("product is already reserved");
	}

	public void delete(int id) throws Exception {
		Reservation reservation = reservationRepository.get(id);
		if (reservation.getStartDate().isAfter(LocalDateTime.now()))
			throw new Exception("cannot remove already started reservation");

		reservation.getCustomer().getReservations().remove(reservation);
		reservation.getProduct().getReservations().remove(reservation);
		reservationRepository.delete(id);
	}

	public Reservation modify (int id, Function<Reservation, Reservation> func) throws Exception {
		return reservationRepository.modify(id, func);
	}

	public Reservation get(int id) {
		return reservationRepository.get(id);
	}

	public Map<Integer, Reservation> get (Predicate<Reservation> predicate) {
		try {
			return reservationRepository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Reservation> getMap () {
		return reservationRepository.getMap();
	}



}
