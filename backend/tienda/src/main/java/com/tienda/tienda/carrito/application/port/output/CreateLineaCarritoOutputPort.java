package com.tienda.tienda.carrito.application.port.output;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import reactor.core.publisher.Mono;

public interface CreateLineaCarritoOutputPort {
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
}