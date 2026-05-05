package com.tienda.carritoservice.application.service;

import com.tienda.carritoservice.domain.model.LineaCarrito;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductLoader {

    public Mono<LineaCarrito> loadProduct(LineaCarrito linea) {
        return Mono.just(linea);
    }
}