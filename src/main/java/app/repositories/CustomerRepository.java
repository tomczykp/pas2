package app.repositories;

import app.model.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class CustomerRepository extends InMemoryRepository<Integer, Customer> {

    @PostConstruct
    public void init() {
        counter = 1;
    }

    private Integer counter;

    @Override
    public Customer insert (Customer v) throws Exception {
        for (Map.Entry<Integer, Customer> i : getMap().entrySet()) {
            if (Objects.equals(i.getValue().getUsername(), v.getUsername())) {
                throw new Exception("Username already exist");
            }
        }
        v.setCustomerID(counter);
        return insert(counter++, v);
    }
}
