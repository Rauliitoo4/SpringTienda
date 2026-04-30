package com.tienda.tienda.promotion.infrastructure.adapter.input.rest;

import com.tienda.tienda.promotion.application.port.input.UpdatePromotionInputPort;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.request.PromotionRequest;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/promociones")
public class UpdatePromotionRestAdapter {

    private final UpdatePromotionInputPort updatePromotionInputPort;
    private final PromotionRestMapper mapper;

    public UpdatePromotionRestAdapter(UpdatePromotionInputPort updatePromotionInputPort, PromotionRestMapper mapper) {
        this.updatePromotionInputPort = updatePromotionInputPort;
        this.mapper = mapper;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PromotionResponse>> updatePromotion(@PathVariable int id, @RequestBody PromotionRequest request) {
        return updatePromotionInputPort.execute(id, mapper.toDomain(request))
                .map(promotion -> ResponseEntity.ok(mapper.toResponse(promotion)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
