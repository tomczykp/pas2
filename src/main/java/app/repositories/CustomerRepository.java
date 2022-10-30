package app.repositories;

import app.model.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository extends InMemoryRepository<Integer, Customer> {

	@PostConstruct
	public void init() {
		counter = 1;
	}

    private Integer counter;

    @Override
    public Customer insert (Customer v) {
        v.setCustomerID(Long.valueOf(counter));
        return insert(counter++, v);
    }
}
