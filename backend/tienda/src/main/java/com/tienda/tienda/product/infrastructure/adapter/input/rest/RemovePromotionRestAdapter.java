package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.usecase.RemovePromotionUseCase;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class RemovePromotionRestAdapter {

    private final RemovePromotionUseCase removePromotionUseCase;
    private final ProductRestMapper mapper;

    public RemovePromotionRestAdapter(RemovePromotionUseCase removePromotionUseCase,
                                      ProductRestMapper mapper) {
        this.removePromotionUseCase = removePromotionUseCase;
        this.mapper = mapper;
    }

    @DeleteMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductResponse>> removePromotion(@PathVariable int productoID,
                                                                 @PathVariable int promocionID) {
        return removePromotionUseCase.execute(productoID, promocionID)
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
