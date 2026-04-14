package com.tienda.tienda.unit.controller;

import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.service.UserService;
import com.tienda.tienda.controller.UserController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private UserResponseDTO usuarioResponse;
    private UserDTO usuarioRequest;

    @BeforeEach
    void setUp() {
        usuarioResponse = new UserResponseDTO();
        usuarioResponse.setId(1);
        usuarioResponse.setNombre("Alberto");
        usuarioResponse.setApellidos("García");
        usuarioResponse.setUsername("albertog");
        usuarioResponse.setEmail("albertog@gmail.com");
        usuarioResponse.setCarritoId(1);

        usuarioRequest = new UserDTO();
        usuarioRequest.setNombre("Alberto");
        usuarioRequest.setApellidos("García");
        usuarioRequest.setUsername("albertog");
        usuarioRequest.setEmail("albertog@gmail.com");
        usuarioRequest.setPassword("1234");
    }

    //GET /usuarios
    @Test
    void getAllUsers_deberiaRetornarListaYStatus200() {
        when(userService.getAllUsers()).thenReturn(Flux.just(usuarioResponse));

        webTestClient.get().uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDTO.class)
                .hasSize(1);

    }

    @Test
    void getAllUsers_listaVacia_deberiaRetornarArrayVacioYStatus200() {
        when(userService.getAllUsers()).thenReturn(Flux.empty());

        webTestClient.get().uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDTO.class)
                .hasSize(0);
    }

    //GET /usuarios/{id}
    @Test
    void getUserById_existente_deberiaRetornarUsuarioyStatus200() {
        when(userService.getUserById(1)).thenReturn(Mono.just(usuarioResponse));

        webTestClient.get().uri("/usuarios/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .isEqualTo(usuarioResponse);
    }

    @Test
    void getUserById_noExistente_deberiaRetornar404() {
        when(userService.getUserById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /usuarios
    @Test
    void createUser_deberiaRetornarUsuarioCreadoYStatus201() {
        when(userService.createUser(any(UserDTO.class))).thenReturn(Mono.just(usuarioResponse));

        webTestClient.post().uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class)
                .isEqualTo(usuarioResponse);
    }

    @Test
    void createUser_deberiaDelegarEnServicio() {
        when(userService.createUser(any(UserDTO.class))).thenReturn(Mono.just(usuarioResponse));

        webTestClient.post().uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioRequest)
                .exchange()
                .expectStatus().isCreated();
        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    //PUT /usuarios/{id}
    @Test
    void updateUser_existente_deberiaRetornarUsuarioActualizadoYStatus200() {
        usuarioResponse.setNombre("Antonio");
        when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(Mono.just(usuarioResponse));

        webTestClient.put().uri("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .isEqualTo(usuarioResponse);
    }

    @Test
    void updateUser_noExistente_deberiaRetornar404() {
        when(userService.updateUser(eq(99), any(UserDTO.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    //DELETE /usuarios/{id}
    @Test
    void deleteUser_existente_deberiaRetornar204() {
        when(userService.deleteUser(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/usuarios/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteUser_noExistente_deberiaRetornar404() {
        when(userService.deleteUser(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}
