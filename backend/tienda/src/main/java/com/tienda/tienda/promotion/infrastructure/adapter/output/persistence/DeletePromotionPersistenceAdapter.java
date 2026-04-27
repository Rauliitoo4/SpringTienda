package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence;

import com.tienda.tienda.promotion.domain.repository.DeletePromotionRepository;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.repository.PromotionJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeletePromotionPersistenceAdapter implements DeletePromotionRepository {

    private final PromotionJpaRepository jpaRepository;

    public DeletePromotionPersistenceAdapter(PromotionJpaRepository jpaRepository) {
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
