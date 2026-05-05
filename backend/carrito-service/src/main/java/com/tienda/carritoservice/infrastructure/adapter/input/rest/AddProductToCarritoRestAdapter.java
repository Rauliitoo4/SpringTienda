package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.port.input.AddProductToCarritoInputPort;
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

    private final AddProductToCarritoInputPort addProductToCarritoInputPort;
    private final CarritoRestMapper carritoRestMapper;
    private final AddProductToCarritoRequestMapper requestMapper;

    public AddProductToCarritoRestAdapter(AddProductToCarritoInputPort addProductToCarritoInputPort, CarritoRestMapper carritoRestMapper, AddProductToCarritoRequestMapper requestMapper) {
        this.addProductToCarritoInputPort = addProductToCarritoInputPort;
        this.carritoRestMapper = carritoRestMapper;
        this.requestMapper = requestMapper;
    }

    @PostMapping("/{carritoId}/productos")
    public Mono<ResponseEntity<CarritoResponse>> addProduct(@PathVariable int carritoId, @RequestBody AddProductToCarritoRequest request) {
        return addProductToCarritoInputPort.execute(carritoId, requestMapper.toProductId(request), requestMapper.toQuantity(request), requestMapper.toSize(request))
                .map(carrito -> ResponseEntity.ok(carritoRestMapper.toResponse(carrito)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}