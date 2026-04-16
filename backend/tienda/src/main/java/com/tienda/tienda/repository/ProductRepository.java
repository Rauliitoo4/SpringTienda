package com.tienda.tienda.repository;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.port.ProductRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> , ProductRepositoryPort {
    Mono<Product> save(Product product);
}
