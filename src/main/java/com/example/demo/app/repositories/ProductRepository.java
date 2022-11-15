package com.example.demo.app.repositories;

import com.example.demo.app.model.Product;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
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
