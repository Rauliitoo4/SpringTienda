package com.tienda.promotionservice.infrastructure.adapter.output.persistence;

import com.tienda.promotionservice.application.port.output.DeletePromotionOutputPort;
import com.tienda.promotionservice.infrastructure.adapter.output.persistence.repository.PromotionR2dbcRepository;
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
