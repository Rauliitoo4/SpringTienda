package com.tienda.userservice.infrastructure.adapter.output.persistence;

import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.application.port.output.UpdateUserOutputPort;
import com.tienda.userservice.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.tienda.userservice.infrastructure.adapter.output.persistence.repository.UserR2dbcRepository;
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