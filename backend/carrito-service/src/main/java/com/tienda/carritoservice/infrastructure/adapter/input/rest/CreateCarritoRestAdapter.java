package com.tienda.carritoservice.infrastructure.adapter.input.rest;

import com.tienda.carritoservice.application.port.input.CreateCarritoInputPort;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.carrito.model.CarritoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carritos")
public class CreateCarritoRestAdapter {

    private final CreateCarritoInputPort createCarritoInputPort;
    private final CarritoRestMapper carritoRestMapper;

    public CreateCarritoRestAdapter(CreateCarritoInputPort createCarritoInputPort,
                                    CarritoRestMapper carritoRestMapper) {
        this.createCarritoInputPort = createCarritoInputPort;
        this.carritoRestMapper = carritoRestMapper;
    }

    @PostMapping
    public Mono<ResponseEntity<CarritoResponse>> create() {
        return createCarritoInputPort.execute()
                .map(carrito -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(carritoRestMapper.toResponse(carrito)));
    }
}