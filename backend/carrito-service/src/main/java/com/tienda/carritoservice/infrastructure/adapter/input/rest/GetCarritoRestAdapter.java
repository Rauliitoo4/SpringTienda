package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.port.input.GetCarritoInputPort;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carritos")
public class GetCarritoRestAdapter {

    private final GetCarritoInputPort getCarritoInputPort;
    private final CarritoRestMapper mapper;

    public GetCarritoRestAdapter(GetCarritoInputPort getCarritoInputPort, CarritoRestMapper mapper) {
        this.getCarritoInputPort = getCarritoInputPort;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CarritoResponse>> getCarritoById(@PathVariable int id) {
        return getCarritoInputPort.execute(id)
                .map(carrito -> ResponseEntity.ok(mapper.toResponse(carrito)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}