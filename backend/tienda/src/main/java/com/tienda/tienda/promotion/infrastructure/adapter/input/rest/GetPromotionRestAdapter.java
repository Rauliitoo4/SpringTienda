package com.tienda.tienda.promotion.infrastructure.adapter.input.rest;

import com.tienda.tienda.promotion.application.usecase.GetPromotionUseCase;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/promociones")
public class GetPromotionRestAdapter {

    private final GetPromotionUseCase getPromotionUseCase;
    private final PromotionRestMapper mapper;

    public GetPromotionRestAdapter(GetPromotionUseCase getPromotionUseCase, PromotionRestMapper mapper) {
        this.getPromotionUseCase = getPromotionUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PromotionResponse>> getPromotionById(@PathVariable int id) {
        return getPromotionUseCase.execute(id)
                .map(promotion -> ResponseEntity.ok(mapper.toResponse(promotion)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<Flux<PromotionResponse>> getAllPromotions() {
        return ResponseEntity.ok(getPromotionUseCase.executeAll()
                .map(mapper::toResponse));
    }
}
