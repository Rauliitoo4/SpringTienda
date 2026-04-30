package com.tienda.tienda.unit.restAdapter.promotion;

import com.tienda.tienda.promotion.application.port.input.DeletePromotionInputPort;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.DeletePromotionRestAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(DeletePromotionRestAdapter.class)
class DeletePromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private DeletePromotionInputPort deletePromotionInputPort;

    @Test
    void deletePromotion_shouldReturn204() {
        when(deletePromotionInputPort.execute(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/promociones/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deletePromotion_ifNotExists_shouldReturn404() {
        when(deletePromotionInputPort.execute(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}