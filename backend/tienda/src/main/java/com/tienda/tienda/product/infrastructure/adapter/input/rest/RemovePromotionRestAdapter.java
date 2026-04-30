package com.tienda.tienda.product.infrastructure.adapter.input.rest;

import com.tienda.tienda.product.application.port.input.RemovePromotionInputPort;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class RemovePromotionRestAdapter {

    private final RemovePromotionInputPort removePromotionInputPort;
    private final ProductRestMapper mapper;

    public RemovePromotionRestAdapter(RemovePromotionInputPort removePromotionInputPort, ProductRestMapper mapper) {
        this.removePromotionInputPort = removePromotionInputPort;
        this.mapper = mapper;
    }

    @DeleteMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductResponse>> removePromotion(@PathVariable int productoID,
                                                                 @PathVariable int promocionID) {
        return removePromotionInputPort.execute(productoID, promocionID)
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
