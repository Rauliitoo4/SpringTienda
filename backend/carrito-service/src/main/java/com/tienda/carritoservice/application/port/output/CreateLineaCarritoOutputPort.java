package com.tienda.carritoservice.application.port.output;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import reactor.core.publisher.Mono;

public interface CreateLineaCarritoOutputPort {
    Mono<LineaCarrito> save(LineaCarrito lineaCarrito);
}