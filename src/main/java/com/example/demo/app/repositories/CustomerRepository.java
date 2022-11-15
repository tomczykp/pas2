package com.example.demo.app.repositories;

import com.example.demo.app.model.Customer;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

@Repository
public class CustomerRepository extends InMemoryRepository<Integer, Customer> {

	@PostConstruct
	public void init() {
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
