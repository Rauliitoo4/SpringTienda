package com.tienda.tienda.lineacarrito.infraestructure.repository;

import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import com.tienda.tienda.lineacarrito.application.port.LineaCarritoRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LineaCarritoRepository extends ReactiveCrudRepository<LineaCarrito, Integer>, LineaCarritoRepositoryPort {
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
}
