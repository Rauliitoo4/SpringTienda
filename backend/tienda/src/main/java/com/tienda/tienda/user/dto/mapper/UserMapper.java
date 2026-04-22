package com.tienda.tienda.user.dto.mapper;

import com.tienda.tienda.user.model.User;
import com.tienda.tienda.user.dto.UserResponseDTO;
import com.tienda.tienda.user.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setCarritoId(user.getCarritoId());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLastname(dto.getLastname());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
