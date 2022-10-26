package app.repositories;

import app.model.a.Customer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository extends InMemoryRepository<Integer, Customer> {
    public CustomerRepository() {
        super();
    }
}
