package com.tienda.tienda.user.infrastructure.adapter.output.persistence;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.UpdateUserRepository;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository.UserR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UpdateUserPersistenceAdapter implements UpdateUserRepository {

    private final UserR2dbcRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    public UpdateUserPersistenceAdapter(UserR2dbcRepository jpaRepository, UserPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> save(User user) {
        return jpaRepository.save(mapper.toEntity(user))
                .map(mapper::toDomain);
    }
}