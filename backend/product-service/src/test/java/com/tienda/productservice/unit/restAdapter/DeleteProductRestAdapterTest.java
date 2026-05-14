package com.tienda.productservice.unit.restAdapter;

import com.tienda.productservice.application.port.input.DeleteProductInputPort;
import com.tienda.productservice.infrastructure.adapter.input.rest.DeleteProductRestAdapter;
import com.tienda.productservice.infrastructure.security.JwtAuthenticationFilter;
import com.tienda.productservice.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(DeleteProductRestAdapter.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class DeleteProductRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private DeleteProductInputPort deleteProductInputPort;

    @Test
    @WithMockUser
    void deleteProduct_shouldReturnStatus204() {
        when(deleteProductInputPort.execute(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/productos/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(deleteProductInputPort, times(1)).execute(1);
    }

    @Test
    @WithMockUser
    void deleteProduct_ifNotExists_shouldReturn404() {
        when(deleteProductInputPort.execute(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}