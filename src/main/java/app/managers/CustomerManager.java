package app.managers;

import app.FunctionThrows;
import app.dto.CustomerDTO;
import app.exceptions.NotFoundException;
import app.model.Customer;
import app.repositories.CustomerRepository;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CustomerManager {

	@Inject
	private CustomerRepository customerRepository;

	synchronized public CustomerDTO create (String username, String email, String password) throws Exception {
		return new CustomerDTO(customerRepository.insert(new Customer(username, email, password)));
	}

	public void delete (int id) {
		customerRepository.delete(id);
	}

	public Customer modify (int id, FunctionThrows<Customer> func) throws Exception {
		return customerRepository.modify(id, func);
	}

	public Customer get (int id) throws NotFoundException {
		return customerRepository.get(id);
	}

	public Map<Integer, CustomerDTO> get (Predicate<Customer> predicate) {
		try {
			HashMap<Integer, CustomerDTO> res = new HashMap<>();
			for (Map.Entry<Integer, Customer> entry : customerRepository.get(predicate).entrySet())
				res.put(entry.getKey(), new CustomerDTO(entry.getValue()));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, CustomerDTO> getMap () {
		HashMap<Integer, CustomerDTO> res = new HashMap<>();
		for (Map.Entry<Integer, Customer> entry : customerRepository.getMap().entrySet())
			res.put(entry.getKey(), new CustomerDTO(entry.getValue()));
		return res;
	}

	public int getLength () {
		return customerRepository.getLenght();
	}

}
