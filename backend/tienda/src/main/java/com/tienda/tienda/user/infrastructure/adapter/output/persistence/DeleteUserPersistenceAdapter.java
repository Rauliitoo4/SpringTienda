package com.tienda.tienda.user.infrastructure.adapter.output.persistence;

import com.tienda.tienda.user.domain.repository.DeleteUserRepository;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository.UserR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteUserPersistenceAdapter implements DeleteUserRepository {

    private final UserR2dbcRepository jpaRepository;

    public DeleteUserPersistenceAdapter(UserR2dbcRepository jpaRepository) {
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