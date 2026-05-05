package com.tienda.carritoservice.application.port.output;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Size;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetLineaCarritoOutputPort {
    Mono<LineaCarrito> findById(int id);
    Flux<LineaCarrito> findAll();
    Flux<LineaCarrito> findByCarritoId(int carritoId);
    Mono<LineaCarrito> findByCarritoIdAndProductIdAndSize(int carritoId, int productId, Size size);
}