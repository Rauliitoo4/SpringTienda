package com.tienda.tienda.repository;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.repository.port.LineaCarritoRepositoryPort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineaCarritoRepository extends ReactiveCrudRepository<LineaCarrito, Integer>, LineaCarritoRepositoryPort {
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
}
