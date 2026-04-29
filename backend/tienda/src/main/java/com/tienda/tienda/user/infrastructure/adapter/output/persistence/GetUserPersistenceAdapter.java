package com.tienda.tienda.user.infrastructure.adapter.output.persistence;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.GetUserRepository;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository.UserR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetUserPersistenceAdapter implements GetUserRepository {

    private final UserR2dbcRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    public GetUserPersistenceAdapter(UserR2dbcRepository jpaRepository, UserPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> findById(int id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<User> findAll() {
        return jpaRepository.findAll()
                .map(mapper::toDomain);
    }
}