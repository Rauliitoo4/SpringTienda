package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.RemoveFavoritoInputPort;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.input.rest.RemoveFavoritoRestAdapter;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(RemoveFavoritoRestAdapter.class)
class RemoveFavoritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RemoveFavoritoInputPort removeFavoritoInputPort;

    @MockitoBean
    private UserRestMapper userRestMapper;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setEmail("alberto@gmail.com");
        user.setFavoritoIds(List.of());

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Alberto");
        userResponse.setEmail("alberto@gmail.com");
        userResponse.setFavoritoIds(List.of());
    }

    @Test
    void removeFavorito_shouldReturnUserAndStatus200() {
        when(removeFavoritoInputPort.execute(1, 1)).thenReturn(Mono.just(user));
        when(userRestMapper.toResponse(user)).thenReturn(userResponse);

        webTestClient.delete().uri("/usuarios/1/favoritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(removeFavoritoInputPort, times(1)).execute(1, 1);
    }

    @Test
    void removeFavorito_ifUserNotExists_shouldReturn404() {
        when(removeFavoritoInputPort.execute(99, 1)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/usuarios/99/favoritos/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}