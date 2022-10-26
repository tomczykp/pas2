package app.repositories;

import app.model.a.Customer;
import app.model.a.Product;
import app.model.a.Reservation;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReservationRepository extends InMemoryRepository<Integer, Reservation>{
    public ReservationRepository() {
        super();
        this.counter = 0;
    }

    private Integer counter;

    @Override
    public Reservation insert (Reservation v) {
        return super.insert(counter++, v);
    }
}
