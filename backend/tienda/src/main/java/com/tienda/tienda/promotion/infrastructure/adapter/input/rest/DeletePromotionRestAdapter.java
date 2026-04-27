package com.tienda.tienda.promotion.infrastructure.adapter.input.rest;

import com.tienda.tienda.promotion.application.usecase.DeletePromotionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/promociones")
public class DeletePromotionRestAdapter {

    private final DeletePromotionUseCase deletePromotionUseCase;

    public DeletePromotionRestAdapter(DeletePromotionUseCase deletePromotionUseCase) {
        this.deletePromotionUseCase = deletePromotionUseCase;
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePromotion(@PathVariable int id) {
        return deletePromotionUseCase.execute(id)
                .map(deleted -> deleted
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
}
