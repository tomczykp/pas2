package app.managers.a;

import app.model.a.Customer;
import app.model.a.Product;
import app.model.a.Reservation;
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

	public Reservation create(LocalDateTime e, Customer c, Product p) throws Exception {
		for (Reservation res: p.getReservations())
			if (res.getEndDate().isAfter(LocalDateTime.now()))
				throw new Exception("Product still in reservation");
		return this.reservationRepository.insert(new Reservation(e, c, p));
	}

	public void delete(int id) throws Exception {
		if (this.reservationRepository.get(id).getEndDate().isAfter(LocalDateTime.now())) {
			throw new Exception("Product still in reservation!");
		}
		this.reservationRepository.get(id).getCustomer().getReservations().remove(this.reservationRepository.get(id));
		this.reservationRepository.get(id).getProduct().getReservations().remove(this.reservationRepository.get(id));
		this.reservationRepository.delete(id);
	}

	public Reservation modify (int id, Function<Reservation, Reservation> func) throws Exception {
		return this.reservationRepository.modify(id, func);
	}

	public Reservation get(int id) {
		return this.reservationRepository.get(id);
	}

	public List<Reservation> get (Predicate<Reservation> predicate) {
		try {
			return this.reservationRepository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Reservation> getMap () {
		return this.reservationRepository.getMap();
	}



}
