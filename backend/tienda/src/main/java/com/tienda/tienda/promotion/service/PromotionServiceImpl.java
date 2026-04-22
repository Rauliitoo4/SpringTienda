package com.tienda.tienda.promotion.service;

import com.tienda.tienda.promotion.dto.PromotionDTO;
import com.tienda.tienda.promotion.dto.mapper.PromotionMapper;
import com.tienda.tienda.promotion.model.Promotion;
import com.tienda.tienda.promotion.repository.port.PromotionRepositoryPort;
import com.tienda.tienda.product.repository.port.ProductPromotionRepositoryPort;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PromotionServiceImpl implements PromotionService {
    
    private final PromotionRepositoryPort promotionRepo;
    private final ProductPromotionRepositoryPort productPromotionRepo;
    private final PromotionMapper promotionMapper;

    public PromotionServiceImpl(PromotionRepositoryPort promotionRepo, ProductPromotionRepositoryPort productPromotionRepo, PromotionMapper promotionMapper){
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
