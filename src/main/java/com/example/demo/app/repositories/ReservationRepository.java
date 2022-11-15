package com.example.demo.app.repositories;

import com.example.demo.app.model.Reservation;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
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
