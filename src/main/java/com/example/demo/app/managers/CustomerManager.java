package com.example.demo.app.managers;

import com.example.demo.app.FunctionThrows;
import com.example.demo.app.dto.CustomerDTO;
import com.example.demo.app.exceptions.NotFoundException;
import com.example.demo.app.model.Customer;
import com.example.demo.app.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class CustomerManager {

	private CustomerRepository customerRepository;

	@Autowired
	public CustomerManager(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public CustomerDTO create (String username, String email, String password) throws Exception {
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
