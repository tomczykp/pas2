package app.repositories;

import app.model.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class CustomerRepository extends InMemoryRepository<Integer, Customer> {

	@PostConstruct
	public void init () {
		counter = 1;
	}

	private Integer counter;

	@Override
	public Customer insert (Customer v) throws Exception {
		v.setCustomerID(counter);
		for (Map.Entry<Integer, Customer> c : getMap().entrySet()) {
			if (Objects.equals(c.getValue().getUsername(), v.getUsername()))
				throw new Exception("Username already exist");
		}
		return insert(counter++, v);
	}

}
