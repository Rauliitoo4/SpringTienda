package com.tienda.promotionservice.infrastructure.adapter.input.rest;

import com.tienda.promotionservice.application.port.input.DeletePromotionInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/promociones")
public class DeletePromotionRestAdapter {

    private final DeletePromotionInputPort deletePromotionInputPort;

    public DeletePromotionRestAdapter(DeletePromotionInputPort deletePromotionInputPort) {
        this.deletePromotionInputPort = deletePromotionInputPort;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePromotion(@PathVariable int id) {
        return deletePromotionInputPort.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}
