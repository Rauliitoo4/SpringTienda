package com.tienda.tienda.carrito.application.port.input;

import com.tienda.tienda.carrito.domain.model.Carrito;
import reactor.core.publisher.Mono;

public interface AddProductToCarritoInputPort {
    Mono<Carrito> execute(int carritoId, int productId, int quantity);
}