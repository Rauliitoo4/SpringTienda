package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.usecase.GetCarritoUseCase;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carritos")
public class GetCarritoRestAdapter {

    private final GetCarritoUseCase getCarritoUseCase;
    private final CarritoRestMapper mapper;

    public GetCarritoRestAdapter(GetCarritoUseCase getCarritoUseCase, CarritoRestMapper mapper) {
        this.getCarritoUseCase = getCarritoUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CarritoResponse>> getCarritoById(@PathVariable int id) {
        return getCarritoUseCase.execute(id)
                .map(carrito -> ResponseEntity.ok(mapper.toResponse(carrito)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}