package com.tienda.tienda.user.service;

import com.tienda.tienda.carrito.service.CarritoService;
import com.tienda.tienda.user.repository.port.UserRepositoryPort;
import com.tienda.tienda.user.dto.UserDTO;
import com.tienda.tienda.user.dto.UserResponseDTO;
import com.tienda.tienda.user.dto.mapper.UserMapper;
import com.tienda.tienda.user.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService{
    
    private final UserRepositoryPort userRepo;
    private final CarritoService carritoService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepositoryPort userRepo, CarritoService carritoService, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.carritoService = carritoService;
        this.userMapper = userMapper;
    }

    public Mono<UserResponseDTO> createUser(UserDTO dto) {
        return carritoService.createCarrito()
                .flatMap(carrito -> {
                    User user = userMapper.toEntity(dto);
                    user.setCarritoId(carrito.getId());
                    return userRepo.save(user);
                })
                .map(userMapper::toDTO);
    }

    public Mono<UserResponseDTO> updateUser(int id, UserDTO dto){
        return userRepo.findById(id)
                .flatMap(user -> {
                    if (dto.getName() != null) user.setName(dto.getName());
                    if (dto.getLastname() != null) user.setLastname(dto.getLastname());
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
