package app.repositories;

import app.model.a.Customer;
import app.model.a.Customer_;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository extends InMemoryRepository<Integer, Customer> {
    public CustomerRepository() {
        super();
        this.counter = 0;
    }

    private Integer counter;

    @Override
    public Customer insert (Customer v) {
        return super.insert(counter++, v);
    }
}
