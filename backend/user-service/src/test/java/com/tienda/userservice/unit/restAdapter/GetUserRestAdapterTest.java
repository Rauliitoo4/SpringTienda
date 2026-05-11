package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.GetUserInputPort;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.input.rest.GetUserRestAdapter;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(GetUserRestAdapter.class)
class GetUserRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetUserInputPort getUserInputPort;

    @MockitoBean
    private UserRestMapper mapper;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setEmail("alberto@gmail.com");
        user.setCarritoId(1);
        user.setFavoritoIds(List.of());

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Alberto");
        userResponse.setEmail("alberto@gmail.com");
        userResponse.setCarritoId(1);
        userResponse.setFavoritoIds(List.of());
    }

    @Test
    void getUserById_shouldReturnUserAndStatus200() {
        when(getUserInputPort.execute(1)).thenReturn(Mono.just(user));
        when(mapper.toResponse(user)).thenReturn(userResponse);

        webTestClient.get().uri("/usuarios/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(getUserInputPort, times(1)).execute(1);
    }

    @Test
    void getUserById_ifNotExists_shouldReturn404() {
        when(getUserInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllUsers_shouldReturnListAndStatus200() {
        when(getUserInputPort.executeAll()).thenReturn(Flux.just(user));
        when(mapper.toResponse(user)).thenReturn(userResponse);

        webTestClient.get().uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(1)
                .contains(userResponse);

        verify(getUserInputPort, times(1)).executeAll();
    }
}