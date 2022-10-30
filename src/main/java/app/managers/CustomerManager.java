package app.managers;

import app.model.Customer;
import app.repositories.CustomerRepository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class CustomerManager {

	@Inject
	private CustomerRepository customerRepository;

	public Customer create(String username, String email, String password) throws Exception{
		for (Map.Entry<Integer, Customer> c: customerRepository.getMap().entrySet()) {
			if (Objects.equals(c.getValue().getUsername(), username))
				throw new Exception("Username already exist");
		}
		return customerRepository.insert(new Customer(username, email, password));
	}

	public void delete(int id) {
		customerRepository.delete(id);
	}

	public Customer modify (int id, Function<Customer, Customer> func) throws Exception {
		return customerRepository.modify(id, func);
	}

	public Customer get(int id) {
		return customerRepository.get(id);
	}

	public List<Customer> get (Predicate<Customer> predicate) {
		try {
			return customerRepository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Customer> getMap () {
		return customerRepository.getMap();
	}

	public int getLength() {
		return customerRepository.getLenght();
	}
}
