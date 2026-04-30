package com.tienda.tienda.promotion.infrastructure.adapter.input.rest;

import com.tienda.tienda.promotion.application.port.input.CreatePromotionInputPort;
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

    private final CreatePromotionInputPort createPromotionInputPort;
    private final PromotionRestMapper mapper;

    public CreatePromotionRestAdapter(CreatePromotionInputPort createPromotionInputPort, PromotionRestMapper mapper) {
        this.createPromotionInputPort = createPromotionInputPort;
        this.mapper = mapper;
    }

    @PostMapping
    public Mono<ResponseEntity<PromotionResponse>> createPromotion(@RequestBody PromotionRequest request){
        return createPromotionInputPort.execute(mapper.toDomain(request))
                .map(promotion -> ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(promotion)));
    }
}
