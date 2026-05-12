package com.tienda.productservice.infrastructure.adapter.input.rest;

import com.tienda.productservice.application.port.input.AddPromotionInputPort;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.product.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
public class AddPromotionRestAdapter {

    private final AddPromotionInputPort addPromotionInputPort;
    private final ProductRestMapper mapper;

    public AddPromotionRestAdapter(AddPromotionInputPort addPromotionInputPort, ProductRestMapper mapper) {
        this.addPromotionInputPort = addPromotionInputPort;
        this.mapper = mapper;
    }

    @PostMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductResponse>> addPromotion(@PathVariable int productoID,
                                                              @PathVariable int promocionID) {
        return addPromotionInputPort.execute(productoID, promocionID)
                .map(product -> ResponseEntity.ok(mapper.toResponse(product)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
