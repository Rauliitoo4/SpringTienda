package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.usecase.GetProductUseCase;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class GetProductRestAdapter {

    private final GetProductUseCase getProductUseCase;
    private final ProductRestMapper mapper;

    public GetProductRestAdapter(GetProductUseCase getProductUseCase, ProductRestMapper mapper) {
        this.getProductUseCase = getProductUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> getProductById(@PathVariable int id) {
        return getProductUseCase.execute(id)
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<Flux<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(getProductUseCase.executeAll()
                .map(mapper::toResponse));
    }
}
