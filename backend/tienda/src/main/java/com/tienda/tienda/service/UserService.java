package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserResponseDTO createUser(UserDTO dto) {
        User user = convertToEntity(dto);

        Carrito carrito = new Carrito();
        carrito.setLineas(new ArrayList<>());
        user.setCarrito(carrito);

        userRepo.save(user);
        return convertToDTO(user);
    }

    public UserResponseDTO updateUser(int id, UserDTO dto){
        User user = userRepo.findById(id).orElse(null);
        if (user == null) return null;

        if (dto.getNombre() != null) user.setNombre(dto.getNombre());
        if (dto.getApellidos() != null) user.setNombre(dto.getApellidos());
        if (dto.getUsername() != null) user.setNombre(dto.getUsername());
        if (dto.getEmail() != null) user.setNombre(dto.getEmail());
        if (dto.getPassword() != null) user.setNombre(dto.getPassword());

        userRepo.save(user);
        return convertToDTO(user);
    }

    public boolean deleteUser(int id) {
        if (!userRepo.existsById(id)) return false;
        userRepo.deleteById(id);
        return true;
    }

    public UserResponseDTO getUserById(int id) {
        return userRepo.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> listDTO = new ArrayList<>();
        for (User user: userRepo.findAll()) {
            listDTO.add(convertToDTO(user));
        }
        return listDTO;
    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNombre(user.getNombre());
        dto.setApellidos(user.getApellidos());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setCarritoId(user.getCarrito().getId());
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
