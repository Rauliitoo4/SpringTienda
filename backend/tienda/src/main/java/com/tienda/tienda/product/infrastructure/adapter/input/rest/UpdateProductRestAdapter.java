package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.usecase.UpdateProductUseCase;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class UpdateProductRestAdapter {

    private final UpdateProductUseCase updateProductUseCase;
    private final ProductRestMapper mapper;

    public UpdateProductRestAdapter(UpdateProductUseCase updateProductUseCase, ProductRestMapper mapper) {
        this.updateProductUseCase = updateProductUseCase;
        this.mapper = mapper;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> updateProduct(@PathVariable int id, @RequestBody ProductRequest request) {
        return updateProductUseCase.execute(id, mapper.toDomain(request))
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

