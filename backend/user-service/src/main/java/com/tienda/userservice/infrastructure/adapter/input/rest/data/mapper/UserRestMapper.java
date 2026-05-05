package com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.response.UserResponse;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRestMapper {
    User toDomain(UserRequest request);
    UserResponse toResponse(User user);
}
