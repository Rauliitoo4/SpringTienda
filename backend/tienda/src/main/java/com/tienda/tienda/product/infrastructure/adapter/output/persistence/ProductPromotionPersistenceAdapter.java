package com.tienda.tienda.product.infrastructure.adapter.output.persistence;

import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.repository.ProductPromotionJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductPromotionPersistenceAdapter implements ProductPromotionRepository {

    private final ProductPromotionJpaRepository jpaRepository;

    public ProductPromotionPersistenceAdapter(ProductPromotionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Flux<Integer> findPromotionIdsByProductId(int productId) {
        return jpaRepository.findPromotionIdsByProductId(productId);
    }

    @Override
    public Mono<Void> deleteByProductIdAndPromotionId(int productId, int promotionId) {
        return jpaRepository.deleteByProductIdAndPromotionId(productId, promotionId);
    }

    @Override
    public Mono<Integer> existsRelation(int productId, int promotionId) {
        return jpaRepository.existsRelation(productId, promotionId);
    }

    @Override
    public Mono<Void> deleteByPromotionId(int promotionId) {
        return jpaRepository.deleteByPromotionId(promotionId);
    }

    @Override
    public Mono<Void> insertRelation(int productId, int promotionId) {
        return jpaRepository.insertRelation(productId, promotionId);
    }
}

