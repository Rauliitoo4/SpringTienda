package com.tienda.tienda.service;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.model.Carrito;
import reactor.core.publisher.Mono;

public interface CarritoService {
    Mono<CarritoDTO> getCarritoById(int id);
    Mono<CarritoDTO> addProductToCarrito(int carritoId, int productId, int quantity);
    Mono<Double> calculateTotal(int carritoId);
    Mono<Carrito> createCarrito();
    Mono<Void> recalculateTotal(int carritoId);
}
