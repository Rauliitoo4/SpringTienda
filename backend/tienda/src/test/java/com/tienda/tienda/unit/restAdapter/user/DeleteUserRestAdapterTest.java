package com.tienda.tienda.unit.restAdapter.user;

import com.tienda.tienda.user.application.usecase.DeleteUserUseCase;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.DeleteUserRestAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(DeleteUserRestAdapter.class)
class DeleteUserRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    void deleteUser_shouldReturn204() {
        when(deleteUserUseCase.execute(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/usuarios/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteUser_ifNotExists_shouldReturn404() {
        when(deleteUserUseCase.execute(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}