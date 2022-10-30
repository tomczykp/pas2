package app.repositories;

import app.model.Customer;
import app.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository extends InMemoryRepository<Integer, Product> {

	@PostConstruct
	public void init() {
		counter = 1;
	}

    private Integer counter;

    @Override
    public Product insert (Product v) {
        v.setId(counter);
        return insert(counter++, v);
    }
}
