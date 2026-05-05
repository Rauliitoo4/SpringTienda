package com.tienda.promotionservice.infrastructure.adapter.output.persistence;

import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.application.port.output.UpdatePromotionOutputPort;
import com.tienda.promotionservice.infrastructure.adapter.output.persistence.mapper.PromotionPersistenceMapper;
import com.tienda.promotionservice.infrastructure.adapter.output.persistence.repository.PromotionR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UpdatePromotionPersistenceAdapter implements UpdatePromotionOutputPort{

    private final PromotionR2dbcRepository r2dbcRepository;
    private final PromotionPersistenceMapper mapper;

    public UpdatePromotionPersistenceAdapter(PromotionR2dbcRepository r2dbcRepository, PromotionPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Promotion> save(Promotion promotion) {
        return r2dbcRepository.save(mapper.toEntity(promotion))
                .map(mapper::toDomain);
    }
}
