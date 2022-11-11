package app.managers;

import app.dto.CustomerDTO;
import app.model.Customer;
import app.repositories.CustomerRepository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class CustomerManager {

	@Inject
	private CustomerRepository customerRepository;

	public CustomerDTO create (String username, String email, String password) throws Exception{
		return new CustomerDTO(customerRepository.insert(new Customer(username, email, password)));
	}

	public void delete(int id) {
		customerRepository.delete(id);
	}

	public Customer modify (int id, Function<Customer, Customer> func) throws Exception {
		return customerRepository.modify(id, func);
	}

	public Customer get(int id) throws Exception {
		return customerRepository.get(id);
	}

	public Map<Integer, Customer> get (Predicate<Customer> predicate) {
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
