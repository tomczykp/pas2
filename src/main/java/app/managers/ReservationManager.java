package app.managers;

import app.FunctionThrows;
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
import java.util.function.Predicate;

public class ReservationManager {

	@Inject
	public ReservationRepository reservationRepository;

	synchronized public ReservationDTO create (Reservation r) throws Exception {
		if (r.getStartDate().isBefore(LocalDate.now()) || r.getEndDate().isBefore(LocalDate.now()))
			throw new Exception("cannot make reservation in the past");

		if (r.getEndDate().isBefore(r.getStartDate()))
			throw new Exception("end date cannot be before start date");

		if (!r.getCustomer().isActive())
			throw new Exception("customer is inactive");

		if (r.getProduct().isReserved(r.getStartDate(), r.getEndDate()))
			throw new Exception("product is already reserved");

		return new ReservationDTO(reservationRepository.insert(r));
	}

	public void delete (int id) throws Exception {
		Reservation reservation = reservationRepository.get(id);
		if ((reservation.getStartDate().isBefore(LocalDate.now()) || reservation.getStartDate().isEqual(LocalDate.now())) && reservation.getEndDate().isAfter(LocalDate.now()))
			throw new Exception("cannot remove already started reservation");

		if (reservation.getStartDate().isAfter(LocalDate.now()))
			throw new Exception("cannot remove future reservation");

		reservation.getCustomer().getReservations().remove(reservation);
		reservation.getProduct().getReservations().remove(reservation);
		reservationRepository.delete(id);
	}

	public Reservation modify (int id, FunctionThrows<Reservation> func) throws Exception {
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
