package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    
    private final List<User> users = new ArrayList<>();

    public UserDTO createUser(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        
        users.add(user);
        return convertToDTO(user);
    }

    public UserDTO updateUser(int id, UserDTO dto){
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

    public UserDTO getUserById(int id) {
        return users.stream()
                    .filter(u -> u.getId() == id)
                    .findFirst()
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> listDTO = new ArrayList<>();
        for (User user: users) {
            listDTO.add(convertToDTO(user));
        }
        return listDTO;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
