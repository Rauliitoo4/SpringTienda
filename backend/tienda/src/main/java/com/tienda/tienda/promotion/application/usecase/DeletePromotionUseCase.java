package com.tienda.tienda.promotion.application.usecase;

import com.tienda.tienda.promotion.domain.repository.DeletePromotionRepository;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeletePromotionUseCase {

    private final DeletePromotionRepository deletePromotionRepository;
    private final ProductPromotionRepository productPromotionRepository;

    public DeletePromotionUseCase(DeletePromotionRepository deletePromotionRepository, ProductPromotionRepository productPromotionRepository) {
        this.deletePromotionRepository = deletePromotionRepository;
        this.productPromotionRepository = productPromotionRepository;
    }

    public Mono<Boolean> execute(int id) {
        return deletePromotionRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return productPromotionRepository.deleteByPromotionId(id)
                            .then(deletePromotionRepository.deleteById(id))
                            .thenReturn(true);
                });
    }
}
