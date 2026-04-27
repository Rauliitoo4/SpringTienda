package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.domain.repository.CreatePromotionRepository;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.mapper.PromotionPersistenceMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.repository.PromotionJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CreatePromotionPersistenceAdapter implements CreatePromotionRepository {

    private final PromotionJpaRepository jpaRepository;
    private final PromotionPersistenceMapper mapper;

    public CreatePromotionPersistenceAdapter(PromotionJpaRepository jpaRepository, PromotionPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Promotion> save(Promotion promotion) {
        return jpaRepository.save(mapper.toEntity(promotion))
                .map(mapper::toDomain);
    }
}
