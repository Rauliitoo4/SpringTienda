package com.tienda.tienda.unit.controller;

import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request.UserRequest;
import com.tienda.tienda.user.application.service.UserService;
import com.tienda.tienda.user.infraestructure.controller.UserController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(UserController.class)
class UserEntityControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Alberto");
        userResponse.setLastname("García");
        userResponse.setUsername("albertog");
        userResponse.setEmail("albertog@gmail.com");
        userResponse.setCarritoId(1);

        userRequest = new UserRequest();
        userRequest.setName("Alberto");
        userRequest.setLastname("García");
        userRequest.setUsername("albertog");
        userRequest.setEmail("albertog@gmail.com");
        userRequest.setPassword("1234");
    }

    //GET /usuarios
    @Test
    void getAllUsers_shouldReturnListAndStatus200() {
        when(userService.getAllUsers()).thenReturn(Flux.just(userResponse));

        webTestClient.get().uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(1);

    }

    @Test
    void getAllUsers_emptyList_shouldReturnEmptyArrayAndStatus200() {
        when(userService.getAllUsers()).thenReturn(Flux.empty());

        webTestClient.get().uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(0);
    }

    //GET /usuarios/{id}
    @Test
    void getUserById_shouldReturnUserAndStatus200() {
        when(userService.getUserById(1)).thenReturn(Mono.just(userResponse));

        webTestClient.get().uri("/usuarios/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    void getUserById_ifNotExists_shouldReturn404() {
        when(userService.getUserById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /usuarios
    @Test
    void createUser_shouldReturnCreatedUserAndStatus201() {
        when(userService.createUser(any(UserRequest.class))).thenReturn(Mono.just(userResponse));

        webTestClient.post().uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    void createUser_shouldCallService() {
        when(userService.createUser(any(UserRequest.class))).thenReturn(Mono.just(userResponse));

        webTestClient.post().uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated();
        verify(userService, times(1)).createUser(any(UserRequest.class));
    }

    //PUT /usuarios/{id}
    @Test
    void updateUser_shouldReturnUpdatedUserAndStatus200() {
        userResponse.setName("Antonio");
        when(userService.updateUser(eq(1), any(UserRequest.class))).thenReturn(Mono.just(userResponse));

        webTestClient.put().uri("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    void updateUser_ifNotExists_shouldReturn404() {
        when(userService.updateUser(eq(99), any(UserRequest.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    //DELETE /usuarios/{id}
    @Test
    void deleteUser_shouldReturn204() {
        when(userService.deleteUser(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/usuarios/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteUser_ifNotExists_shouldReturn404() {
        when(userService.deleteUser(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}
