package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.dto.mapper.UserMapper;
import com.tienda.tienda.model.User;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.repository.port.UserRepositoryPort;
import com.tienda.tienda.service.CarritoService;
import com.tienda.tienda.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    
    @Mock
    private UserRepositoryPort userRepo;

    @Mock
    private CarritoService carritoService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User testingUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setLastname("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setPassword("1234");
        user.setCarritoId(1);
        return user;
    }

    private UserResponseDTO testingDto() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1);
        dto.setName("Alberto");
        dto.setLastname("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setCarritoId(1);
        return dto;
    }

    @Test
    void getUserById_shouldReturn_User() {
        when(userRepo.findById(1)).thenReturn(Mono.just(testingUser()));
        when(userMapper.toDTO(any(User.class))).thenReturn(testingDto());

        StepVerifier.create(userServiceImpl.getUserById(1))
                .expectNextMatches(dto ->
                        dto.getName().equals("Alberto") &&
                        dto.getLastname().equals("García"))
                .verifyComplete();
    }

    @Test
    void getUserById_ifNotExists_shouldReturnNull() {
        when(userRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(userServiceImpl.getUserById(999))
                .verifyComplete();
    }

    @Test
    void createUser_shouldReturnAndSaveDTO() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);

        User savedUser = testingUser();

        when(carritoService.createCarrito()).thenReturn(Mono.just(carrito));
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(savedUser);
        when(userRepo.save(any(User.class))).thenReturn(Mono.just(savedUser));
        when(userMapper.toDTO(any(User.class))).thenReturn(testingDto());

        UserDTO dto = new UserDTO();
        dto.setName("Alberto");
        dto.setLastname("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");

        StepVerifier.create(userServiceImpl.createUser(dto))
                .expectNextMatches(result -> result.getName().equals("Alberto"))
                .verifyComplete();
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_shouldReturnTrue() {
        when(userRepo.existsById(1)).thenReturn(Mono.just(true));
        when(userRepo.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(userServiceImpl.deleteUser(1))
                .expectNext(true)
                .verifyComplete();
        verify(userRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteUser_ifNotExists_shouldReturnFalse() {
        when(userRepo.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(userServiceImpl.deleteUser(999))
                .expectNext(false)
                .verifyComplete();
        verify(userRepo, never()).deleteById(anyInt());
    }

    @Test
    void updateUser_shouldUpdate_User() {
        User user = testingUser();
        UserResponseDTO updatedDto = testingDto();
        updatedDto.setUsername("albertitog");

        when(userRepo.findById(1)).thenReturn(Mono.just(user));
        when(userRepo.save(any(User.class))).thenReturn(Mono.just(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(updatedDto);

        UserDTO dto = new UserDTO();
        dto.setUsername("albertitog");

        StepVerifier.create(userServiceImpl.updateUser(1, dto))
                .expectNextMatches(result -> result.getUsername().equals("albertitog"))
                .verifyComplete();
    }
}
