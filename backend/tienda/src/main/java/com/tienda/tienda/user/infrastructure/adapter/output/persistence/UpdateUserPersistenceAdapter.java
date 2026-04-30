package com.tienda.tienda.user.infrastructure.adapter.output.persistence;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.application.port.output.UpdateUserOutputPort;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository.UserR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UpdateUserPersistenceAdapter implements UpdateUserOutputPort {

    private final UserR2dbcRepository r2dbcRepository;
    private final UserPersistenceMapper mapper;

    public UpdateUserPersistenceAdapter(UserR2dbcRepository r2dbcRepository, UserPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> save(User user) {
        return r2dbcRepository.save(mapper.toEntity(user))
                .map(mapper::toDomain);
    }
}