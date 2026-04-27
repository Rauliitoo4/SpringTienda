package com.tienda.tienda.user.infrastructure.adapter.output.persistence.mapper;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setLastname(user.getLastname());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setCarritoId(user.getCarritoId());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setLastname(entity.getLastname());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setCarritoId(entity.getCarritoId());
        return user;
    }
}
