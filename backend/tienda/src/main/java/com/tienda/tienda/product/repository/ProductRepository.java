package com.tienda.tienda.product.repository;

import com.tienda.tienda.product.model.Product;
import com.tienda.tienda.product.repository.port.ProductRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> , ProductRepositoryPort {
    Mono<Product> save(Product product);
}
