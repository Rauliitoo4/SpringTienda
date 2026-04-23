package com.tienda.tienda.product.infraestructure.repository;

import com.tienda.tienda.product.domain.Product;
import com.tienda.tienda.product.application.port.ProductRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> , ProductRepositoryPort {
    Mono<Product> save(Product product);
}
