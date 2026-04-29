package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence;

import com.tienda.tienda.promotion.domain.repository.DeletePromotionRepository;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.repository.PromotionR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeletePromotionPersistenceAdapter implements DeletePromotionRepository {

    private final PromotionR2dbcRepository jpaRepository;

    public DeletePromotionPersistenceAdapter(PromotionR2dbcRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Boolean> existsById(int id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return jpaRepository.deleteById(id);
    }
}
