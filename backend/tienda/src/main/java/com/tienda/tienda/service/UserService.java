package com.tienda.tienda.service;

import com.tienda.tienda.dto.mapper.UserMapper;
import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.CarritoRepository;
import com.tienda.tienda.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    
    private final UserRepository userRepo;
    private final CarritoService carritoService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepo, CarritoService carritoService, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.carritoService = carritoService;
        this.userMapper = userMapper;
    }

    public Mono<UserResponseDTO> createUser(UserDTO dto) {
        return carritoService.createCarrito()
                .flatMap(carritoGuardado -> {
                    User user = userMapper.toEntity(dto);
                    user.setCarritoId(carritoGuardado.getId());
                    return userRepo.save(user);
                })
                .map(userMapper::toDTO);
    }

    public Mono<UserResponseDTO> updateUser(int id, UserDTO dto){
        return userRepo.findById(id)
                .flatMap(user -> {
                    if (dto.getNombre() != null) user.setNombre(dto.getNombre());
                    if (dto.getApellidos() != null) user.setApellidos(dto.getApellidos());
                    if (dto.getUsername() != null) user.setUsername(dto.getUsername());
                    if (dto.getEmail() != null) user.setEmail(dto.getEmail());
                    if (dto.getPassword() != null) user.setPassword(dto.getPassword());
                    return userRepo.save(user);
                })
                .map(userMapper::toDTO);
    }

    public Mono<Boolean> deleteUser(int id) {
        return userRepo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return userRepo.deleteById(id).thenReturn(true);
                });
    }

    public Mono<UserResponseDTO> getUserById(int id) {
        return userRepo.findById(id)
                    .map(userMapper::toDTO);
    }

    public Flux<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .map(userMapper::toDTO);
    }
}
