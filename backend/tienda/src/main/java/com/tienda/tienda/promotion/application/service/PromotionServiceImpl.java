package com.tienda.tienda.promotion.application.service;

import com.tienda.tienda.promotion.application.dto.PromotionResponse;
import com.tienda.tienda.promotion.application.dto.mapper.PromotionMapper;
import com.tienda.tienda.promotion.domain.Promotion;
import com.tienda.tienda.promotion.application.port.PromotionRepositoryPort;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PromotionServiceImpl implements PromotionService {
    
    private final PromotionRepositoryPort promotionRepo;
    private final ProductPromotionRepository productPromotionRepo;
    private final PromotionMapper promotionMapper;

    public PromotionServiceImpl(PromotionRepositoryPort promotionRepo, ProductPromotionRepository productPromotionRepo, PromotionMapper promotionMapper){
        this.promotionRepo = promotionRepo;
        this.productPromotionRepo = productPromotionRepo;
        this.promotionMapper = promotionMapper;
    }

    public Mono<PromotionResponse> createPromotion(PromotionResponse dto) {
        Promotion promocion = promotionMapper.toEntity(dto);
        return promotionRepo.save(promocion)
                .map(promotionMapper::toDTO);
    }

    public Mono<PromotionResponse> updatePromotion(int id, PromotionResponse dto) {
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

    public Mono<PromotionResponse> getPromotionById (int id){
        return promotionRepo.findById(id)
                    .map(promotionMapper::toDTO);
    }

    public Flux<PromotionResponse> getAllPromotions() {
        return promotionRepo.findAll()
                .map(promotionMapper::toDTO);
    }
}
