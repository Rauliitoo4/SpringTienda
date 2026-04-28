package com.tienda.tienda.unit.restAdapter.product;

import com.tienda.tienda.product.application.usecase.DeleteProductUseCase;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.DeleteProductRestAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(DeleteProductRestAdapter.class)
class DeleteProductRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private DeleteProductUseCase deleteProductUseCase;

    @Test
    void deleteProduct_shouldReturn204() {
        when(deleteProductUseCase.execute(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/productos/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteProduct_ifNotExists_shouldReturn404() {
        when(deleteProductUseCase.execute(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}