package com.tienda.tienda.carrito.application.service;

import com.tienda.tienda.carrito.application.dto.CarritoDTO;
import com.tienda.tienda.carrito.domain.Carrito;
import reactor.core.publisher.Mono;

public interface CarritoService {
    Mono<CarritoDTO> getCarritoById(int id);
    Mono<CarritoDTO> addProductToCarrito(int carritoId, int productId, int quantity);
    Mono<Double> calculateTotal(int carritoId);
    Mono<Carrito> createCarrito();
    Mono<Void> recalculateTotal(int carritoId);
}
