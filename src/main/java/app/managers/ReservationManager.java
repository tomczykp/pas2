package app.managers;

import app.dto.ReservationDTO;
import app.exceptions.NotFoundException;
import app.model.Customer;
import app.model.Product;
import app.model.Reservation;
import app.repositories.ReservationRepository;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReservationManager {

	@Inject
	private ReservationRepository reservationRepository;

	public ReservationDTO create (LocalDate beginDate, LocalDate endDate, Customer c, Product p) throws Exception {

		if (beginDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now()))
			throw new Exception("cannot make reservation in the past");

		if (endDate.isBefore(beginDate))
			throw new Exception("end date cannot be before start date");

		if (!c.isActive())
			throw new Exception("customer is inactive");

		if (p.isReserved(beginDate, endDate))
			throw new Exception("product is already reserved");

		return new ReservationDTO(reservationRepository.insert(new Reservation(beginDate, endDate, c, p)));
	}

	public void delete (int id) throws Exception {
		Reservation reservation = reservationRepository.get(id);
		if (reservation.getStartDate().isAfter(LocalDate.now()))
			throw new Exception("cannot remove already started reservation");

		reservation.getCustomer().getReservations().remove(reservation);
		reservation.getProduct().getReservations().remove(reservation);
		reservationRepository.delete(id);
	}

	public Reservation modify (int id, Function<Reservation, Reservation> func) throws NotFoundException {
		return reservationRepository.modify(id, func);
	}

	public Reservation get (int id) throws NotFoundException {
		return reservationRepository.get(id);
	}

	public Map<Integer, ReservationDTO> get (Predicate<Reservation> predicate) {
		try {
			HashMap<Integer, ReservationDTO> res = new HashMap<>();
			for (Map.Entry<Integer, Reservation> entry : reservationRepository.get(predicate).entrySet())
				res.put(entry.getKey(), new ReservationDTO(entry.getValue()));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, ReservationDTO> getMap () {

		HashMap<Integer, ReservationDTO> res = new HashMap<>();
		for (Map.Entry<Integer, Reservation> entry : reservationRepository.getMap().entrySet())
			res.put(entry.getKey(), new ReservationDTO(entry.getValue()));
		return res;
	}


}
