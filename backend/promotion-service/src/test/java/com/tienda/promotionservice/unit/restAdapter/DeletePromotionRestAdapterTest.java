package com.tienda.promotionservice.unit.restAdapter;

import com.tienda.promotionservice.application.port.input.DeletePromotionInputPort;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.DeletePromotionRestAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
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
    void deletePromotion_shouldReturnStatus204() {
        when(deletePromotionInputPort.execute(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/promociones/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(deletePromotionInputPort, times(1)).execute(1);
    }

    @Test
    void deletePromotion_ifNotExists_shouldReturn404() {
        when(deletePromotionInputPort.execute(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}