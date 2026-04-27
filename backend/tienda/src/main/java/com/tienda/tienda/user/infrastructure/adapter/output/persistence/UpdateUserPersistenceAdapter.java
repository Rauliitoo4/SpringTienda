package com.tienda.tienda.user.infrastructure.adapter.output.persistence;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.UpdateUserRepository;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UpdateUserPersistenceAdapter implements UpdateUserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    public UpdateUserPersistenceAdapter(UserJpaRepository jpaRepository, UserPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> save(User user) {
        return jpaRepository.save(mapper.toEntity(user))
                .map(mapper::toDomain);
    }
}