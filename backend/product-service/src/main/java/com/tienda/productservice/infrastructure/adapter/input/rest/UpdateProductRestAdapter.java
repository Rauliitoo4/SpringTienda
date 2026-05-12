package com.tienda.productservice.infrastructure.adapter.input.rest;

import com.tienda.productservice.application.port.input.UpdateProductInputPort;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.product.model.ProductRequest;
import com.tienda.product.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class UpdateProductRestAdapter {

    private final UpdateProductInputPort updateProductInputPort;
    private final ProductRestMapper mapper;

    public UpdateProductRestAdapter(UpdateProductInputPort updateProductInputPort, ProductRestMapper mapper) {
        this.updateProductInputPort = updateProductInputPort;
        this.mapper = mapper;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> updateProduct(@PathVariable int id, @RequestBody ProductRequest request) {
        return updateProductInputPort.execute(id, mapper.toDomain(request))
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

