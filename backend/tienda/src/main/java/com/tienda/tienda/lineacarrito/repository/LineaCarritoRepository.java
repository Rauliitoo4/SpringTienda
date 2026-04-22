package com.tienda.tienda.lineacarrito.repository;

import com.tienda.tienda.lineacarrito.model.LineaCarrito;
import com.tienda.tienda.lineacarrito.repository.port.LineaCarritoRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LineaCarritoRepository extends ReactiveCrudRepository<LineaCarrito, Integer>, LineaCarritoRepositoryPort {
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
}
