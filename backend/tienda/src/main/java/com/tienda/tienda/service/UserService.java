package com.tienda.tienda.service;

import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.dto.UserResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponseDTO> createUser(UserDTO dto);
    Mono<UserResponseDTO> updateUser(int id, UserDTO dto);
    Mono<Boolean> deleteUser(int id);
    Mono<UserResponseDTO> getUserById(int id);
    Flux<UserResponseDTO> getAllUsers();
}
