package app.managers;

import app.model.a.Customer;
import app.repository.InMemoryRepository;
import app.repository.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CustomerManager {

	private final Repository<Integer, Customer> repository;
	private int counter;
	public CustomerManager() {
		this.repository = new InMemoryRepository<>();
		this.counter = 0;
	}

	public Customer create(String username, String email) {
		return this.repository.insert(this.counter++, new Customer(username, email));
	}

	public void delete(int id) {
		this.repository.delete(id);
	}

	public Customer modify (int id, Function<Customer, Customer> func) throws Exception {
		return this.repository.modify(id, func);
	}

	public Customer get(int id) {
		return this.repository.get(id);
	}

	public List<Customer> get (Predicate<Customer> predicate) {
		try {
			return this.repository.get(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<Integer, Customer> getMap () {
		return this.repository.getMap();
	}

	public int getLength() {
		return this.repository.getLenght();
	}
}
