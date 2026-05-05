package com.tienda.userservice.adapter.output.persistence.mapper;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    UserEntity toEntity(User user);
    User toDomain(UserEntity entity);
}
