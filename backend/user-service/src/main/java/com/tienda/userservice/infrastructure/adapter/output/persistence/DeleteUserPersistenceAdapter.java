package com.tienda.userservice.infrastructure.adapter.output.persistence;

import com.tienda.userservice.application.port.output.DeleteUserOutputPort;
import com.tienda.userservice.infrastructure.adapter.output.persistence.repository.UserR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DeleteUserPersistenceAdapter implements DeleteUserOutputPort {

    private final UserR2dbcRepository r2dbcRepository;

    public DeleteUserPersistenceAdapter(UserR2dbcRepository r2dbcRepository) {
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