package com.tienda.tienda.carrito.infraestructure.repository;

import com.tienda.tienda.carrito.domain.Carrito;
import com.tienda.tienda.carrito.application.port.CarritoRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CarritoRepository extends ReactiveCrudRepository<Carrito, Integer> , CarritoRepositoryPort {
    Mono<Carrito> save(Carrito carrito);
}
