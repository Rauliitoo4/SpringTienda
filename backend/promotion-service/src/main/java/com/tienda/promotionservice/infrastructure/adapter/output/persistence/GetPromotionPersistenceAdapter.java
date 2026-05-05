package com.tienda.promotionservice.infrastructure.adapter.output.persistence;

import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.application.port.output.GetPromotionOutputPort;
import com.tienda.promotionservice.infrastructure.adapter.output.persistence.mapper.PromotionPersistenceMapper;
import com.tienda.promotionservice.infrastructure.adapter.output.persistence.repository.PromotionR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetPromotionPersistenceAdapter implements GetPromotionOutputPort {

    private final PromotionR2dbcRepository r2dbcRepository;
    private final PromotionPersistenceMapper mapper;

    public GetPromotionPersistenceAdapter(PromotionR2dbcRepository r2dbcRepository, PromotionPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Promotion> findById(int id){
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Promotion> findAll(){
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }
}
