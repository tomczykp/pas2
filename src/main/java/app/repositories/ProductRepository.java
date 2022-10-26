package app.repositories;

import app.model.a.Customer;
import app.model.a.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository extends InMemoryRepository<Integer, Product> {
    public ProductRepository(){
        super();
        this.counter = 1;
    }

    private Integer counter;

    @Override
    public Product insert (Product v) {
        v.setId(counter);
        return super.insert(counter++, v);
    }
}
