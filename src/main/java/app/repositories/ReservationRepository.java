package app.repositories;

import app.model.Reservation;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReservationRepository extends InMemoryRepository<Integer, Reservation>{

	@PostConstruct
	public void init() {
		counter = 1;
	}

    private Integer counter;

    @Override
    public Reservation insert (Reservation v) {
        v.setId(counter);
        return insert(counter++, v);
    }
}
