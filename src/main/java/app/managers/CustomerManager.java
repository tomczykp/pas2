package app.managers;

import app.dto.CustomerDTO;
import app.exceptions.NotFoundException;
import app.exceptions.UnauthorizedActionException;
import app.model.Customer;
import app.model.CustomerType;
import app.repositories.CustomerRepository;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CustomerManager {

	@Inject
	private CustomerRepository customerRepository;

	synchronized public CustomerDTO create (Customer customer) throws Exception {
		return new CustomerDTO(customerRepository.insert(customer));
	}

	public Customer get (int id) throws NotFoundException, UnauthorizedActionException {
		CustomerType type = this.customerRepository.get(id).getType();
		if (type == CustomerType.CUSTOMER) {
			return customerRepository.get(id);
		} else {
			throw new UnauthorizedActionException(type);
		}
	}

	synchronized public void delete (int id) throws Exception {
		CustomerType type = this.customerRepository.get(id).getType();
		if (this.customerRepository.get(id).getType() == CustomerType.CUSTOMER) {
			customerRepository.delete(id);
		} else {
			throw new UnauthorizedActionException(type);
		}
	}

	public Map<Integer, CustomerDTO> get (Predicate<Customer> predicate) {
		try {
			HashMap<Integer, CustomerDTO> res = new HashMap<>();
			for (Map.Entry<Integer, Customer> entry : customerRepository.get(predicate).entrySet()) {
				if (entry.getValue().getType() == CustomerType.CUSTOMER) {
					res.put(entry.getKey(), new CustomerDTO(entry.getValue()));
				}
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, CustomerDTO> getMap () {
			HashMap<Integer, CustomerDTO> res = new HashMap<>();
			for (Map.Entry<Integer, Customer> entry : customerRepository.getMap().entrySet()) {
				if (entry.getValue().getType() == CustomerType.CUSTOMER) {
					res.put(entry.getKey(), new CustomerDTO(entry.getValue()));
				}
			}
			return res;
	}
}
