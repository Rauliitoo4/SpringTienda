package com.tienda.userservice.infrastructure.adapter.output.persistence;

import com.tienda.tienda.user.application.port.output.UserFavoritoOutputPort;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository.UserFavoritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserFavoritoPersistenceAdapter implements UserFavoritoOutputPort {

    private final UserFavoritoR2dbcRepository r2dbcRepository;

    public UserFavoritoPersistenceAdapter(UserFavoritoR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Flux<Integer> findProductIdsByUserId(int userId) {
        return r2dbcRepository.findProductIdsByUserId(userId);
    }

    @Override
    public Mono<Integer> existsRelation(int userId, int productId) {
        return r2dbcRepository.existsRelation(userId, productId);
    }

    @Override
    public Mono<Void> insertRelation(int userId, int productId) {
        return r2dbcRepository.insertRelation(userId, productId);
    }

    @Override
    public Mono<Void> deleteRelation(int userId, int productId) {
        return r2dbcRepository.deleteRelation(userId, productId);
    }
}
