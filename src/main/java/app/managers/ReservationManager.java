package app.managers;

import app.model.Customer;
import app.model.Product;
import app.model.Reservation;
import app.repository.InMemoryRepository;
import app.repository.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReservationManager {

	private final Repository<Integer, Reservation> repository;
	private int counter;

	public ReservationManager() {
		this.repository = new InMemoryRepository<>();
		this.counter = 0;
	}

	public Reservation create(LocalDateTime e, Customer c, Product p) throws Exception {
		for (Reservation res: p.getReservations())
			if (res.getEndDate().isAfter(LocalDateTime.now()))
				throw new Exception("Product still in reservation");
		return this.repository.insert(this.counter++, new Reservation(e, c, p));
	}

	public void delete(int id) {
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
