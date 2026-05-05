package com.tienda.tienda.product.infrastructure.adapter.output.persistence;

import com.tienda.tienda.product.application.port.output.ProductPromotionOutputPort;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.repository.ProductPromotionR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductPromotionPersistenceAdapter implements ProductPromotionOutputPort {

    private final ProductPromotionR2dbcRepository r2dbcRepository;

    public ProductPromotionPersistenceAdapter(ProductPromotionR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Flux<Integer> findPromotionIdsByProductId(int productId) {
        return r2dbcRepository.findPromotionIdsByProductId(productId);
    }

    @Override
    public Mono<Void> deleteByProductIdAndPromotionId(int productId, int promotionId) {
        return r2dbcRepository.deleteByProductIdAndPromotionId(productId, promotionId);
    }

    @Override
    public Mono<Integer> existsRelation(int productId, int promotionId) {
        return r2dbcRepository.existsRelation(productId, promotionId);
    }

    @Override
    public Mono<Void> deleteByPromotionId(int promotionId) {
        return r2dbcRepository.deleteByPromotionId(promotionId);
    }

    @Override
    public Mono<Void> insertRelation(int productId, int promotionId) {
        return r2dbcRepository.insertRelation(productId, promotionId);
    }
}

