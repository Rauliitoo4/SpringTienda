package com.tienda.tienda.unit.restAdapter.user;

import com.tienda.tienda.user.application.port.input.AddFavoritoInputPort;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.AddFavoritoRestAdapter;
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

@WebFluxTest(AddFavoritoRestAdapter.class)
class AddFavoritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AddFavoritoInputPort addFavoritoInputPort;

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
        basicUser.setFavoritoIds(List.of(1));

        basicUserResponse = new UserResponse();
        basicUserResponse.setId(1);
        basicUserResponse.setName("Alberto");
        basicUserResponse.setCarritoId(1);
        basicUserResponse.setFavoritoIds(List.of(1));
    }

    @Test
    void addFavorito_shouldReturnUserWithFavoritoAndStatus200() {
        when(addFavoritoInputPort.execute(1, 1)).thenReturn(Mono.just(basicUser));
        when(userRestMapper.toResponse(basicUser)).thenReturn(basicUserResponse);

        webTestClient.post().uri("/usuarios/1/favoritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(basicUserResponse);
    }

    @Test
    void addFavorito_ifUserNotExists_shouldReturn404() {
        when(addFavoritoInputPort.execute(999, 1)).thenReturn(Mono.empty());

        webTestClient.post().uri("/usuarios/999/favoritos/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addFavorito_ifProductNotExists_shouldReturn404() {
        when(addFavoritoInputPort.execute(1, 999)).thenReturn(Mono.empty());

        webTestClient.post().uri("/usuarios/1/favoritos/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}