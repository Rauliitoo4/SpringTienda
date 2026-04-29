package com.tienda.tienda.user.infrastructure.adapter.output.persistence.repository;

import com.tienda.tienda.user.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, Integer>{
}
