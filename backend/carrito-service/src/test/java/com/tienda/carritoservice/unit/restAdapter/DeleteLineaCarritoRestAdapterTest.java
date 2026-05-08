package com.tienda.carritoservice.unit.restAdapter;

import com.tienda.carritoservice.application.port.input.DeleteLineaCarritoInputPort;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.DeleteLineaCarritoRestAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(DeleteLineaCarritoRestAdapter.class)
class DeleteLineaCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private DeleteLineaCarritoInputPort deleteLineaCarritoInputPort;

    @Test
    void deleteLinea_shouldReturnStatus204() {
        when(deleteLineaCarritoInputPort.execute(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/lineas/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteLineaCarritoInputPort, times(1)).execute(1);
    }

    @Test
    void deleteLinea_ifNotExists_shouldReturn404() {
        when(deleteLineaCarritoInputPort.execute(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/lineas/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}