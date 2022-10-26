package app.managers.a;

import app.model.a.Customer;
import app.model.a.Product;
import app.model.a.Reservation;
import app.repositories.ReservationRepository;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReservationManager {

	@Inject
	private ReservationRepository repository;

	public Reservation create(LocalDateTime e, Customer c, Product p) throws Exception {
		for (Reservation res: p.getReservations())
			if (res.getEndDate().isAfter(LocalDateTime.now()))
				throw new Exception("Product still in reservation");
		return this.repository.insert(new Reservation(e, c, p));
	}

	public void delete(int id) throws Exception{
		if (this.repository.get(id).getEndDate().isAfter(LocalDateTime.now())) {
			throw new Exception("Product still in reservation!");
		}
		this.repository.get(id).getCustomer().getReservations().remove(this.repository.get(id));
		this.repository.get(id).getProduct().getReservations().remove(this.repository.get(id));
		this.repository.delete(id);
	}

	public Reservation modify (int id, Function<Reservation, Reservation> func) throws Exception {
		return this.repository.modify(id, func);
	}

	public Reservation get(int id) {
		return this.repository.get(id);
	}

	public List<Reservation> get (Predicate<Reservation> predicate) {
		try {
			return this.repository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Reservation> getMap () {
		return this.repository.getMap();
	}



}
