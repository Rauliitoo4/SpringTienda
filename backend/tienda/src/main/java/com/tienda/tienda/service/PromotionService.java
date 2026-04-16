package com.tienda.tienda.service;

import com.tienda.tienda.dto.*;
import com.tienda.tienda.model.*;
import com.tienda.tienda.repository.ProductPromotionRepository;
import org.springframework.stereotype.Service;
import com.tienda.tienda.dto.mapper.PromotionMapper;

import com.tienda.tienda.repository.PromotionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PromotionService {
    
    private final PromotionRepository promotionRepo;
    private final ProductPromotionRepository productPromotionRepo;
    private final PromotionMapper promotionMapper;

    public PromotionService(PromotionRepository promotionRepo, ProductPromotionRepository productPromotionRepo, PromotionMapper promotionMapper){
        this.promotionRepo = promotionRepo;
        this.productPromotionRepo = productPromotionRepo;
        this.promotionMapper = promotionMapper;
    }

    public Mono<PromotionDTO> createPromotion(PromotionDTO dto) {
        Promotion promocion = promotionMapper.toEntity(dto);
        return promotionRepo.save(promocion)
                .map(promotionMapper::toDTO);
    }

    public Mono<PromotionDTO> updatePromotion(int id, PromotionDTO dto) {
        return promotionRepo.findById(id)
                .flatMap(promo -> {
                    if (dto.getDescription() != null) promo.setDescription(dto.getDescription());
                    if (dto.getDiscount() >= 0) promo.setDiscount(dto.getDiscount());
                    return promotionRepo.save(promo);
                })
                .map(promotionMapper::toDTO);
    }

    public Mono<Boolean> deletePromotion (int id) {
        return promotionRepo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return productPromotionRepo.deleteByPromotionId(id)
                            .then(promotionRepo.deleteById(id))
                            .thenReturn(true);
                });
    }

    public Mono<PromotionDTO> getPromotionById (int id){
        return promotionRepo.findById(id)
                    .map(promotionMapper::toDTO);
    }

    public Flux<PromotionDTO> getAllPromotions() {
        return promotionRepo.findAll()
                .map(promotionMapper::toDTO);
    }
}
