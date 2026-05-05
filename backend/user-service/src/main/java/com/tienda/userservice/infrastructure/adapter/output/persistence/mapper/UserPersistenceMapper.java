package com.tienda.userservice.infrastructure.adapter.output.persistence.mapper;

import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    UserEntity toEntity(User user);
    User toDomain(UserEntity entity);
}
