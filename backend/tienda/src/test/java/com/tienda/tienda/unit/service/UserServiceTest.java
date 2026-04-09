package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.model.User;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.repository.UserRepository;
import com.tienda.tienda.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    void obtenerUsuarioPorId_deberiaDevolver_elUsuario() {
        //Given
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        carrito.setLineas(new ArrayList<>());

        User user = new User();
        user.setId(1);
        user.setNombre("Alberto");
        user.setApellidos("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setPassword("1234");
        user.setCarrito(carrito);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        //When
        UserResponseDTO resultado = userService.getUserById(1);
        
        //Then 
        assertNotNull(resultado);
        assertEquals("Alberto", resultado.getNombre());
        assertEquals("García", resultado.getApellidos());
    }

    @Test
    void obtenerUsuarioPorId_siNoExiste_deberiaDevolverNull() {
        //Given
        when(userRepo.findById(999)).thenReturn(Optional.empty());

        //When
        UserResponseDTO resultado = userService.getUserById(999);

        //Then
        assertNull(resultado);
    }

    @Test
    void crearUsuario_deberiaGuardaryDevolverDTO() {
        //Given
        UserDTO dto = new UserDTO();
        dto.setNombre("Alberto");
        dto.setApellidos("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");

        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        carrito.setLineas(new ArrayList<>());

        User userGuardado = new User();
        userGuardado.setId(1);
        userGuardado.setNombre("Alberto");
        userGuardado.setApellidos("García");
        userGuardado.setUsername("albertog");
        userGuardado.setEmail("albertog@gmail.com");
        userGuardado.setPassword("1234");
        userGuardado.setCarrito(carrito);
        when(userRepo.save(any(User.class))).thenReturn(userGuardado);

        //When
        UserResponseDTO resultado = userService.createUser(dto);

        //Then
        assertNotNull(resultado);
        assertEquals("Alberto", resultado.getNombre());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void eliminarUsuario_siExiste_deberiaDevolverTrue() {
        //Given
        when(userRepo.existsById(1)).thenReturn(true);

        //When
        boolean resultado = userService.deleteUser(1);

        //Then
        assertTrue(resultado);
        verify(userRepo, times(1)).deleteById(1);
    }

    @Test
    void eliminarUsuario_siNoExiste_deberiaDevolverFalse() {
        //Given
        when(userRepo.existsById(999)).thenReturn(false);

        //When
        boolean resultado = userService.deleteUser(999);

        //Then
        assertFalse(resultado);
        verify(userRepo, never()).deleteById(999);
    }

    @Test
    void actualizarUsuario_deberiaModificar_elUsuario() {
        //Given 
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        carrito.setLineas(new ArrayList<>());

        User user = new User();
        user.setId(1);
        user.setNombre("Alberto");
        user.setApellidos("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setPassword("1234");
        user.setCarrito(carrito);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserDTO dto = new UserDTO();
        dto.setUsername("albertitog");

        //When
        UserResponseDTO resultado = userService.updateUser(1, dto);

        //Then
        assertNotNull(resultado);
        assertEquals("albertitog", resultado.getUsername());
    }
}
