package com.tienda.tienda.lineacarrito.service;

import com.tienda.tienda.lineacarrito.dto.LineaCarritoDTO;
import com.tienda.tienda.product.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineaCarritoService {
    Mono<LineaCarritoDTO> updateLinea(int id, int quantity);
    Mono<Boolean> deleteLinea(int id);
    Mono<LineaCarritoDTO> getLineaById(int id);
    Flux<LineaCarritoDTO> getAllLineas();
    Mono<Void> updateLineasCarrito(Product product);
}
