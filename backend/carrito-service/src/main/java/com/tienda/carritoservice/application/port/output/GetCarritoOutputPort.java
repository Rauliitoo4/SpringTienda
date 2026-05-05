package com.tienda.carritoservice.application.port.output;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface GetCarritoOutputPort {
    Mono<Carrito> findById(int id);
}