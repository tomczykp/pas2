package app.managers.a;

import app.model.a.Customer;
import app.repositories.CustomerRepository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CustomerManager {

	@Inject
	private CustomerRepository customerRepository;

	public Customer create(String username, String email) {
		return this.customerRepository.insert(new Customer(username, email));
	}

	public void delete(int id) {
		this.customerRepository.delete(id);
	}

	public Customer modify (int id, Function<Customer, Customer> func) throws Exception {
		return this.customerRepository.modify(id, func);
	}

	public Customer get(int id) {
		return this.customerRepository.get(id);
	}

	public List<Customer> get (Predicate<Customer> predicate) {
		try {
			return this.customerRepository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Customer> getMap () {
		return this.customerRepository.getMap();
	}

	public int getLength() {
		return this.customerRepository.getLenght();
	}
}
