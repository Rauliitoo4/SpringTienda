package com.tienda.tienda.carrito.repository;

import com.tienda.tienda.carrito.model.Carrito;
import com.tienda.tienda.carrito.repository.port.CarritoRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CarritoRepository extends ReactiveCrudRepository<Carrito, Integer> , CarritoRepositoryPort {
    Mono<Carrito> save(Carrito carrito);
}
