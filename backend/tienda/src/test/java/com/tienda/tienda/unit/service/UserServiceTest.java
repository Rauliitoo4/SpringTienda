package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.model.User;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.repository.CarritoRepository;
import com.tienda.tienda.repository.UserRepository;
import com.tienda.tienda.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepo;

    @Mock
    private CarritoRepository carritoRepo;

    @InjectMocks
    private UserService userService;

    private User usuarioDePrueba() {
        User user = new User();
        user.setId(1);
        user.setNombre("Alberto");
        user.setApellidos("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setPassword("1234");
        user.setCarritoId(1);
        return user;
    }

    @Test
    void obtenerUsuarioPorId_deberiaDevolver_elUsuario() {
        when(userRepo.findById(1)).thenReturn(Mono.just(usuarioDePrueba()));

        StepVerifier.create(userService.getUserById(1))
                .expectNextMatches(dto ->
                        dto.getNombre().equals("Alberto") &&
                        dto.getApellidos().equals("García"))
                .verifyComplete();
    }

    @Test
    void obtenerUsuarioPorId_siNoExiste_deberiaDevolverNull() {
        when(userRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(userService.getUserById(999))
                .verifyComplete();
    }

    @Test
    void crearUsuario_deberiaGuardaryDevolverDTO() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);

        User userGuardado = usuarioDePrueba();

        when(carritoRepo.save(any(Carrito.class))).thenReturn(Mono.just(carrito));
        when(userRepo.save(any(User.class))).thenReturn(Mono.just(userGuardado));

        UserDTO dto = new UserDTO();
        dto.setNombre("Alberto");
        dto.setApellidos("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");

        StepVerifier.create(userService.createUser(dto))
                .expectNextMatches(resultado -> resultado.getNombre().equals("Alberto"))
                .verifyComplete();
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void eliminarUsuario_siExiste_deberiaDevolverTrue() {
        when(userRepo.existsById(1)).thenReturn(Mono.just(true));
        when(userRepo.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUser(1))
                .expectNext(true)
                .verifyComplete();
        verify(userRepo, times(1)).deleteById(1);
    }

    @Test
    void eliminarUsuario_siNoExiste_deberiaDevolverFalse() {
        when(userRepo.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(userService.deleteUser(999))
                .expectNext(false)
                .verifyComplete();
        verify(userRepo, never()).deleteById(anyInt());
    }

    @Test
    void actualizarUsuario_deberiaModificar_elUsuario() {
        User user = usuarioDePrueba();
        when(userRepo.findById(1)).thenReturn(Mono.just(user));
        when(userRepo.save(any(User.class))).thenReturn(Mono.just(user));

        UserDTO dto = new UserDTO();
        dto.setUsername("albertitog");

        StepVerifier.create(userService.updateUser(1, dto))
                .expectNextMatches(resultado -> resultado.getUsername().equals("albertitog"))
                .verifyComplete();
    }
}
