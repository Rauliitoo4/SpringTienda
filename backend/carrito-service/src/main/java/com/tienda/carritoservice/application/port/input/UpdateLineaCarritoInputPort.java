package com.tienda.carritoservice.application.port.input;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import reactor.core.publisher.Mono;

public interface UpdateLineaCarritoInputPort {
    Mono<LineaCarrito> execute(int id, int quantity);
}