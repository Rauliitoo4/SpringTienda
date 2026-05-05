package com.tienda.carritoservice.application.port.input;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.product.domain.model.Size;
import reactor.core.publisher.Mono;

public interface AddProductToCarritoInputPort {
    Mono<Carrito> execute(int carritoId, int productId, int quantity, Size size);
}