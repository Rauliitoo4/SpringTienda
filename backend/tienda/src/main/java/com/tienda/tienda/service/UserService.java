package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    
    private final List<User> users = new ArrayList<>();
    private final CarritoService carritoService;

    public UserService(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    public UserResponseDTO createUser(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setNombre(dto.getNombre());
        user.setApellidos(dto.getApellidos());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        CarritoDTO carritoDTO = carritoService.createCarrito();
        Carrito carrito = new Carrito();
        carrito.setId(carritoDTO.getId());
        carrito.setLineas(new ArrayList<>());
        user.setCarrito(carrito);
        
        users.add(user);
        return convertToDTO(user);
    }

    public UserResponseDTO updateUser(int id, UserDTO dto){
        for (User user: users) {
            if (user.getId() == id) {
                if (dto.getUsername() != null) user.setUsername(dto.getUsername());
                if (dto.getEmail() != null) user.setEmail(dto.getEmail());
                if (dto.getPassword() != null) user.setPassword(dto.getPassword());
                return convertToDTO(user);
            }
        }
        return null;
    }

    public boolean deleteuser(int id) {
        return users.removeIf(u -> u.getId() == id);
    }

    public UserResponseDTO getUserById(int id) {
        return users.stream()
                    .filter(u -> u.getId() == id)
                    .findFirst()
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> listDTO = new ArrayList<>();
        for (User user: users) {
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
        return dto;
    }
}
