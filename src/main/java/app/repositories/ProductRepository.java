package app.repositories;

import app.model.a.Customer;
import app.model.a.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository extends InMemoryRepository<Integer, Product> {
    public ProductRepository(){
        super();
    }
}
