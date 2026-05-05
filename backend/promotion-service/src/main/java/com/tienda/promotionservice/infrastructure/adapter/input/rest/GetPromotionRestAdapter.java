package com.tienda.promotionservice.infrastructure.adapter.input.rest;

import com.tienda.promotionservice.application.port.input.GetPromotionInputPort;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/promociones")
public class GetPromotionRestAdapter {

    private final GetPromotionInputPort getPromotionInputPort;
    private final PromotionRestMapper mapper;

    public GetPromotionRestAdapter(GetPromotionInputPort getPromotionInputPort, PromotionRestMapper mapper) {
        this.getPromotionInputPort = getPromotionInputPort;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PromotionResponse>> getPromotionById(@PathVariable int id) {
        return getPromotionInputPort.execute(id)
                .map(promotion -> ResponseEntity.ok(mapper.toResponse(promotion)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<Flux<PromotionResponse>> getAllPromotions() {
        return ResponseEntity.ok(getPromotionInputPort.executeAll()
                .map(mapper::toResponse));
    }
}
