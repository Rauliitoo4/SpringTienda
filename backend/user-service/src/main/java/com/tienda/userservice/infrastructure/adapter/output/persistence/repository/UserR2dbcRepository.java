package com.tienda.userservice.infrastructure.adapter.output.persistence.repository;

import com.tienda.userservice.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, Integer>{
    Mono<UserEntity> findByEmail(String email);
}
