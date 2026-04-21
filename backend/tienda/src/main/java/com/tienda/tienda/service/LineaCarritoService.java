package com.tienda.tienda.service;

import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineaCarritoService {
    Mono<LineaCarritoDTO> updateLinea(int id, int quantity);
    Mono<Boolean> deleteLinea(int id);
    Mono<LineaCarritoDTO> getLineaById(int id);
    Flux<LineaCarritoDTO> getAllLineas();
    Mono<Void> updateLineasCarrito(Product product);
}
