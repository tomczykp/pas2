package app.managers;

import app.model.Customer;
import app.model.Product;
import app.model.Reservation;
import app.repositories.ReservationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReservationManager {

	@Inject
	private ReservationRepository reservationRepository;

	public Reservation create(LocalDateTime b, LocalDateTime e, Customer c, Product p) throws Exception {
		for (Reservation res: p.getReservations())
			if (res.getEndDate().isAfter(LocalDateTime.now()))
				throw new Exception("Product still in reservation");
		return reservationRepository.insert(new Reservation(b, e, c, p));
	}

	public void delete(int id) throws Exception {
		if (reservationRepository.get(id).getEndDate().isAfter(LocalDateTime.now())) {
			throw new Exception("Product still in reservation!");
		}
		reservationRepository.get(id).getCustomer().getReservations().remove(reservationRepository.get(id));
		reservationRepository.get(id).getProduct().getReservations().remove(reservationRepository.get(id));
		reservationRepository.delete(id);
	}

	public Reservation modify (int id, Function<Reservation, Reservation> func) throws Exception {
		return reservationRepository.modify(id, func);
	}

	public Reservation get(int id) {
		return reservationRepository.get(id);
	}

	public List<Reservation> get (Predicate<Reservation> predicate) {
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
