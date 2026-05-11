package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.DeleteUserInputPort;
import com.tienda.userservice.infrastructure.adapter.input.rest.DeleteUserRestAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(DeleteUserRestAdapter.class)
class DeleteUserRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private DeleteUserInputPort deleteUserInputPort;

    @Test
    void deleteUser_shouldReturnStatus204() {
        when(deleteUserInputPort.execute(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/usuarios/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteUserInputPort, times(1)).execute(1);
    }

    @Test
    void deleteUser_ifNotExists_shouldReturn404() {
        when(deleteUserInputPort.execute(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/usuarios/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}