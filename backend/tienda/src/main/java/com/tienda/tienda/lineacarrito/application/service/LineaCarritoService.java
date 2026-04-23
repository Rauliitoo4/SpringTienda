package com.tienda.tienda.lineacarrito.application.service;

import com.tienda.tienda.lineacarrito.application.dto.LineaCarritoDTO;
import com.tienda.tienda.product.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineaCarritoService {
    Mono<LineaCarritoDTO> updateLinea(int id, int quantity);
    Mono<Boolean> deleteLinea(int id);
    Mono<LineaCarritoDTO> getLineaById(int id);
    Flux<LineaCarritoDTO> getAllLineas();
    Mono<Void> updateLineasCarrito(Product product);
}
