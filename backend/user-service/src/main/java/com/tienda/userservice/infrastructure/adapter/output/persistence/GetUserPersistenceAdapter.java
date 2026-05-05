package com.tienda.userservice.infrastructure.adapter.output.persistence;

import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.tienda.userservice.infrastructure.adapter.output.persistence.repository.UserR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetUserPersistenceAdapter implements GetUserOutputPort {

    private final UserR2dbcRepository r2dbcRepository;
    private final UserPersistenceMapper mapper;

    public GetUserPersistenceAdapter(UserR2dbcRepository r2dbcRepository, UserPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> findById(int id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<User> findAll() {
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByEmailAndPassword(String email, String password) {
        return r2dbcRepository.findByEmailAndPassword(email, password)
                .map(mapper::toDomain);
    }
}