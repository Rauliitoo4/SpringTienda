package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.domain.repository.GetPromotionRepository;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.mapper.PromotionPersistenceMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.repository.PromotionJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetPromotionPersistenceAdapter implements GetPromotionRepository {

    private final PromotionJpaRepository jpaRepository;
    private final PromotionPersistenceMapper mapper;

    public GetPromotionPersistenceAdapter(PromotionJpaRepository jpaRepository, PromotionPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Promotion> findById(int id){
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Promotion> findAll(){
        return jpaRepository.findAll()
                .map(mapper::toDomain);
    }
}
