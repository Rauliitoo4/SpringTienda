package com.tienda.tienda.promotion.infrastructure.adapter.output.persistence;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.domain.repository.GetPromotionRepository;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.mapper.PromotionPersistenceMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.repository.PromotionR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetPromotionPersistenceAdapter implements GetPromotionRepository {

    private final PromotionR2dbcRepository jpaRepository;
    private final PromotionPersistenceMapper mapper;

    public GetPromotionPersistenceAdapter(PromotionR2dbcRepository jpaRepository, PromotionPersistenceMapper mapper) {
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
