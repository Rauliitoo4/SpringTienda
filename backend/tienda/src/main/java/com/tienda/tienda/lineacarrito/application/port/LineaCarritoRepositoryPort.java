package com.tienda.tienda.lineacarrito.application.port;

import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineaCarritoRepositoryPort {
    Mono<LineaCarrito> findById(int id);
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
    Flux<LineaCarrito> findAll();
    Flux<LineaCarrito> findByCarritoId(int carritoId);
    Mono<Void> deleteById(int id);
}
