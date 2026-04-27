package com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRestMapper {

    public User toDomain(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return user;
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setLastname(user.getLastname());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setCarritoId(user.getCarritoId());
        return response;
    }
}
