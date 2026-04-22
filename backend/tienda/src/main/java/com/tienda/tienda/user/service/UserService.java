package com.tienda.tienda.user.service;

import com.tienda.tienda.user.dto.UserDTO;
import com.tienda.tienda.user.dto.UserResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponseDTO> createUser(UserDTO dto);
    Mono<UserResponseDTO> updateUser(int id, UserDTO dto);
    Mono<Boolean> deleteUser(int id);
    Mono<UserResponseDTO> getUserById(int id);
    Flux<UserResponseDTO> getAllUsers();
}
