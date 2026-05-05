package com.tienda.carritoservice.application.port.output;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import reactor.core.publisher.Mono;

public interface UpdateLineaCarritoOutputPort {
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
}