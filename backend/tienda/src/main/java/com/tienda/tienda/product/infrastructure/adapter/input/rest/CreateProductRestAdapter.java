package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.usecase.CreateProductUseCase;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class CreateProductRestAdapter {

    private final CreateProductUseCase createProductUseCase;
    private final ProductRestMapper mapper;

    public CreateProductRestAdapter(CreateProductUseCase createProductUseCase, ProductRestMapper mapper) {
        this.createProductUseCase = createProductUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public Mono<ResponseEntity<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        return createProductUseCase.execute(mapper.toDomain(request))
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(product)));
    }
}
