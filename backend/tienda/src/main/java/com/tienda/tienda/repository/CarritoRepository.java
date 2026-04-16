package com.tienda.tienda.repository;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.repository.port.CarritoRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CarritoRepository extends ReactiveCrudRepository<Carrito, Integer> , CarritoRepositoryPort {
    Mono<Carrito> save(Carrito carrito);
}
