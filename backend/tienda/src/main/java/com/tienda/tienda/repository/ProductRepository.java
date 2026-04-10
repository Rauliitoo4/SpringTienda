package com.tienda.tienda.repository;

import com.tienda.tienda.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
    
}
