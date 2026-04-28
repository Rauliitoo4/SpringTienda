package com.tienda.tienda.unit.restAdapter.user;

import com.tienda.tienda.user.application.usecase.GetUserUseCase;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.GetUserRestAdapter;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(GetUserRestAdapter.class)
class GetUserRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetUserUseCase getUserUseCase;

    @MockitoBean
    private UserRestMapper userRestMapper;

    private User basicUser;
    private UserResponse basicUserResponse;

    @BeforeEach
    void setUp() {
        basicUser = new User();
        basicUser.setId(1);
        basicUser.setName("Alberto");
        basicUser.setLastname("García");
        basicUser.setUsername("albertog");
        basicUser.setEmail("albertog@gmail.com");
        basicUser.setCarritoId(1);

        basicUserResponse = new UserResponse();
        basicUserResponse.setId(1);
        basicUserResponse.setName("Alberto");
        basicUserResponse.setLastname("García");
        basicUserResponse.setUsername("albertog");
        basicUserResponse.setEmail("albertog@gmail.com");
        basicUserResponse.setCarritoId(1);
    }

    @Test
    void getAllUsers_shouldReturnListAndStatus200() {
        when(getUserUseCase.executeAll()).thenReturn(Flux.just(basicUser));
        when(userRestMapper.toResponse(basicUser)).thenReturn(basicUserResponse);

        webTestClient.get().uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(1);
    }

    @Test
    void getAllUsers_emptyList_shouldReturnEmptyArrayAndStatus200() {
        when(getUserUseCase.executeAll()).thenReturn(Flux.empty());

        webTestClient.get().uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(0);
    }

    @Test
    void getUserById_shouldReturnUserAndStatus200() {
        when(getUserUseCase.execute(1)).thenReturn(Mono.just(basicUser));
        when(userRestMapper.toResponse(basicUser)).thenReturn(basicUserResponse);

        webTestClient.get().uri("/usuarios/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(basicUserResponse);
    }

    @Test
    void getUserById_ifNotExists_shouldReturn404() {
        when(getUserUseCase.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}