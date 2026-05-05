package com.tienda.productservice.infrastructure.adapter.input.rest;

import com.tienda.productservice.application.port.input.GetProductInputPort;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class GetProductRestAdapter {

    private final GetProductInputPort getProductInputPort;
    private final ProductRestMapper mapper;

    public GetProductRestAdapter(GetProductInputPort getProductInputPort, ProductRestMapper mapper) {
        this.getProductInputPort = getProductInputPort;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> getProductById(@PathVariable int id) {
        return getProductInputPort.execute(id)
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<Flux<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(getProductInputPort.executeAll()
                .map(mapper::toResponse));
    }
}
