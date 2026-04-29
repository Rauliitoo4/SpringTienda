package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.usecase.AddProductToCarritoUseCase;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.AddProductToCarritoRequestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.request.AddProductToCarritoRequest;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carritos")
public class AddProductToCarritoRestAdapter {

    private final AddProductToCarritoUseCase addProductToCarritoUseCase;
    private final CarritoRestMapper carritoRestMapper;
    private final AddProductToCarritoRequestMapper requestMapper;

    public AddProductToCarritoRestAdapter(AddProductToCarritoUseCase addProductToCarritoUseCase, CarritoRestMapper carritoRestMapper, AddProductToCarritoRequestMapper requestMapper) {
        this.addProductToCarritoUseCase = addProductToCarritoUseCase;
        this.carritoRestMapper = carritoRestMapper;
        this.requestMapper = requestMapper;
    }

    @PostMapping("/{carritoId}/productos")
    public Mono<ResponseEntity<CarritoResponse>> addProduct(@PathVariable int carritoId, @RequestBody AddProductToCarritoRequest request) {
        return addProductToCarritoUseCase.execute(carritoId, requestMapper.toProductId(request), requestMapper.toQuantity(request))
                .map(carrito -> ResponseEntity.ok(carritoRestMapper.toResponse(carrito)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}