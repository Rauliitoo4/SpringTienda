package com.tienda.tienda.controller;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.service.PromotionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/promociones")
public class PromotionController {
    
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService){
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<Flux<PromotionDTO>> getAllUsers() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PromotionDTO>> getPromotionById(@PathVariable int id) {
        return promotionService.getPromotionById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<PromotionDTO>> createPromotion(@RequestBody PromotionDTO dto) {
        return promotionService.createPromotion(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PromotionDTO>> updatePromotion(@PathVariable int id, @RequestBody PromotionDTO dto) {
        return promotionService.updatePromotion(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePromotion(@PathVariable int id) {
        return promotionService.deletePromotion(id)
                .map(d -> d
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
    
    
}
