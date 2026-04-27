package com.tienda.tienda.promotion.infrastructure.adapter.input.rest;

import com.tienda.tienda.promotion.application.usecase.CreatePromotionUseCase;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.request.PromotionRequest;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/promociones")
public class CreatePromotionRestAdapter {

    private final CreatePromotionUseCase createPromotionUseCase;
    private final PromotionRestMapper mapper;

    public CreatePromotionRestAdapter(CreatePromotionUseCase createPromotionUseCase, PromotionRestMapper mapper) {
        this.createPromotionUseCase = createPromotionUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public Mono<ResponseEntity<PromotionResponse>> createPromotion(@RequestBody PromotionRequest request){
        return createPromotionUseCase.execute(mapper.toDomain(request))
                .map(promotion -> ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(promotion)));
    }
}
