package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.usecase.AddPromotionUseCase;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class AddPromotionRestAdapter {

    private final AddPromotionUseCase addPromotionUseCase;
    private final ProductRestMapper mapper;

    public AddPromotionRestAdapter(AddPromotionUseCase addPromotionUseCase,
                                   ProductRestMapper mapper) {
        this.addPromotionUseCase = addPromotionUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductResponse>> addPromotion(@PathVariable int productoID,
                                                              @PathVariable int promocionID) {
        return addPromotionUseCase.execute(productoID, promocionID)
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
