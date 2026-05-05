package com.tienda.carritoservice.application.port.output;

import com.tienda.carritoservice.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface GetCarritoOutputPort {
    Mono<Carrito> findById(int id);
}