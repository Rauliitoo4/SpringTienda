package com.tienda.tienda.dto.mapper;

import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNombre(user.getNombre());
        dto.setApellidos(user.getApellidos());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setCarritoId(user.getCarritoId());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setNombre(dto.getNombre());
        user.setApellidos(dto.getApellidos());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
