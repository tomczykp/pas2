package com.example.demo.app.managers;

import com.example.demo.app.FunctionThrows;
import com.example.demo.app.dto.ReservationDTO;
import com.example.demo.app.exceptions.NotFoundException;
import com.example.demo.app.model.Customer;
import com.example.demo.app.model.Product;
import com.example.demo.app.model.Reservation;
import com.example.demo.app.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class ReservationManager {

	private ReservationRepository reservationRepository;

	@Autowired
	public ReservationManager(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

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
