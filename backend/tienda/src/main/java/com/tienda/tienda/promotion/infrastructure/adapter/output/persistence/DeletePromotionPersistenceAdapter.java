package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence;

import com.tienda.tienda.promotion.application.port.output.DeletePromotionOutputPort;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.repository.PromotionR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeletePromotionPersistenceAdapter implements DeletePromotionOutputPort {

    private final PromotionR2dbcRepository r2dbcRepository;

    public DeletePromotionPersistenceAdapter(PromotionR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<Boolean> existsById(int id) {
        return r2dbcRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(int id) {
        return r2dbcRepository.deleteById(id);
    }
}
