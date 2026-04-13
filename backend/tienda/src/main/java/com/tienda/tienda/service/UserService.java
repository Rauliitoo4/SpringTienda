package com.tienda.tienda.service;

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
    private final CarritoRepository carritoRepo;

    public UserService(UserRepository userRepo, CarritoRepository carritoRepo) {
        this.userRepo = userRepo;
        this.carritoRepo = carritoRepo;
    }

    public Mono<UserResponseDTO> createUser(UserDTO dto) {
        Carrito carrito = new Carrito();
        carrito.setTotal(0.0);

        return carritoRepo.save(carrito)
                .flatMap(carritoGuardado -> {
                    User user = convertToEntity(dto);
                    user.setCarritoId(carritoGuardado.getId());
                    return userRepo.save(user);
                })
                .map(this::convertToDTO);
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
                .map(this::convertToDTO);
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
                    .map(this::convertToDTO);
    }

    public Flux<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .map(this::convertToDTO);
    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNombre(user.getNombre());
        dto.setApellidos(user.getApellidos());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setCarritoId(user.getCarritoId());
        return dto;
    }

    private User convertToEntity (UserDTO dto) {
        User user = new User();
        user.setNombre(dto.getNombre());
        user.setApellidos(dto.getApellidos());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
