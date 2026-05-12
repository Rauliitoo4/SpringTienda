package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.AddFavoritoInputPort;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.input.rest.AddFavoritoRestAdapter;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.user.model.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
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

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setEmail("alberto@gmail.com");
        user.setFavoritoIds(List.of(1));

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Alberto");
        userResponse.setEmail("alberto@gmail.com");
        userResponse.setFavoritoIds(List.of(1));
    }

    @Test
    void addFavorito_shouldReturnUserAndStatus200() {
        when(addFavoritoInputPort.execute(1, 1)).thenReturn(Mono.just(user));
        when(userRestMapper.toResponse(user)).thenReturn(userResponse);

        webTestClient.post().uri("/usuarios/1/favoritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(addFavoritoInputPort, times(1)).execute(1, 1);
    }

    @Test
    void addFavorito_ifUserNotExists_shouldReturn404() {
        when(addFavoritoInputPort.execute(99, 1)).thenReturn(Mono.empty());

        webTestClient.post().uri("/usuarios/99/favoritos/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}