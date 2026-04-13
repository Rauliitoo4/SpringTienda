package com.tienda.tienda.service;

import com.tienda.tienda.dto.*;
import com.tienda.tienda.model.*;
import com.tienda.tienda.repository.ProductoPromocionRepository;
import org.springframework.stereotype.Service;

import com.tienda.tienda.repository.ProductRepository;
import com.tienda.tienda.repository.PromotionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PromotionService {
    
    private final PromotionRepository promotionRepo;
    private final ProductoPromocionRepository productoPromocionRepo;

    public PromotionService(PromotionRepository promotionRepo, ProductoPromocionRepository productoPromocionRepo){
        this.promotionRepo = promotionRepo;
        this.productoPromocionRepo = productoPromocionRepo;
    }

    public Mono<PromotionDTO> createPromotion(PromotionDTO dto) {
        Promotion promocion = convertToEntity(dto);
        return promotionRepo.save(promocion)
                .map(this::convertToDTO);
    }

    public Mono<PromotionDTO> updatePromotion(int id, PromotionDTO dto) {
        return promotionRepo.findById(id)
                .flatMap(promo -> {
                    if (dto.getDescripcion() != null) promo.setDescripcion(dto.getDescripcion());
                    if (dto.getDescuento() >= 0) promo.setDescuento(dto.getDescuento());
                    return promotionRepo.save(promo);
                })
                .map(this::convertToDTO);
    }

    public Mono<Boolean> deletePromotion (int id) {
        return promotionRepo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return productoPromocionRepo.deleteByPromotionId(id)
                            .then(promotionRepo.deleteById(id))
                            .thenReturn(true);
                });
    }

    public Mono<PromotionDTO> getPromotionById (int id){
        return promotionRepo.findById(id)
                    .map(this::convertToDTO);
    }

    public Flux<PromotionDTO> getAllPromotions() {
        return promotionRepo.findAll()
                .map(this::convertToDTO);
    }

    private PromotionDTO convertToDTO (Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(promotion.getId());
        dto.setDescuento(promotion.getDescuento());
        dto.setDescripcion(promotion.getDescripcion());
        return dto;
    }

    private Promotion convertToEntity (PromotionDTO dto) {
        Promotion promo = new Promotion();
        promo.setDescripcion(dto.getDescripcion());
        promo.setDescuento(dto.getDescuento());
        return promo;
    }
}
