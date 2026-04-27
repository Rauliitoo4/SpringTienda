package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.usecase.AddProductToCarritoUseCase;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carritos")
public class AddProductToCarritoRestAdapter {

    private final AddProductToCarritoUseCase addProductToCarritoUseCase;
    private final CarritoRestMapper mapper;

    public AddProductToCarritoRestAdapter(AddProductToCarritoUseCase addProductToCarritoUseCase, CarritoRestMapper mapper) {
        this.addProductToCarritoUseCase = addProductToCarritoUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/{carritoId}/productos/{productId}")
    public Mono<ResponseEntity<CarritoResponse>> addProduct(@PathVariable int carritoId, @PathVariable int productId, @RequestParam int quantity) {
        return addProductToCarritoUseCase.execute(carritoId, productId, quantity)
                .map(carrito -> ResponseEntity.ok(mapper.toResponse(carrito)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}