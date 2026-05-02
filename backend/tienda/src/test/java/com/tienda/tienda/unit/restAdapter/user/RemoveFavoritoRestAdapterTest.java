package com.tienda.tienda.unit.restAdapter.user;

import com.tienda.tienda.user.application.port.input.RemoveFavoritoInputPort;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.RemoveFavoritoRestAdapter;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
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

    private User basicUser;
    private UserResponse basicUserResponse;

    @BeforeEach
    void setUp() {
        basicUser = new User();
        basicUser.setId(1);
        basicUser.setName("Alberto");
        basicUser.setCarritoId(1);
        basicUser.setFavoritoIds(List.of());

        basicUserResponse = new UserResponse();
        basicUserResponse.setId(1);
        basicUserResponse.setName("Alberto");
        basicUserResponse.setCarritoId(1);
        basicUserResponse.setFavoritoIds(List.of());
    }

    @Test
    void removeFavorito_shouldReturnUserWithoutFavoritoAndStatus200() {
        when(removeFavoritoInputPort.execute(1, 1)).thenReturn(Mono.just(basicUser));
        when(userRestMapper.toResponse(basicUser)).thenReturn(basicUserResponse);

        webTestClient.delete().uri("/usuarios/1/favoritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(basicUserResponse);
    }

    @Test
    void removeFavorito_ifUserNotExists_shouldReturn404() {
        when(removeFavoritoInputPort.execute(999, 1)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/usuarios/999/favoritos/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}