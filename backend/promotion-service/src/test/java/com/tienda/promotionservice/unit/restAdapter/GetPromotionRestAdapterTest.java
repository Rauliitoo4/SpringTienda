package com.tienda.promotionservice.unit.restAdapter;

import com.tienda.promotionservice.application.port.input.GetPromotionInputPort;
import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.GetPromotionRestAdapter;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(GetPromotionRestAdapter.class)
class GetPromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetPromotionInputPort getPromotionInputPort;

    @MockitoBean
    private PromotionRestMapper mapper;

    private Promotion promotion;
    private PromotionResponse promotionResponse;

    @BeforeEach
    void setUp() {
        promotion = new Promotion();
        promotion.setId(1);
        promotion.setDiscount(10.0);
        promotion.setDescription("Descuento verano");

        promotionResponse = new PromotionResponse();
        promotionResponse.setId(1);
        promotionResponse.setDiscount(10.0);
        promotionResponse.setDescription("Descuento verano");
    }

    @Test
    void getPromotionById_shouldReturnPromotionAndStatus200() {
        when(getPromotionInputPort.execute(1)).thenReturn(Mono.just(promotion));
        when(mapper.toResponse(promotion)).thenReturn(promotionResponse);

        webTestClient.get().uri("/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PromotionResponse.class)
                .isEqualTo(promotionResponse);

        verify(getPromotionInputPort, times(1)).execute(1);
    }

    @Test
    void getPromotionById_ifNotExists_shouldReturn404() {
        when(getPromotionInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllPromotions_shouldReturnListAndStatus200() {
        when(getPromotionInputPort.executeAll()).thenReturn(Flux.just(promotion));
        when(mapper.toResponse(promotion)).thenReturn(promotionResponse);

        webTestClient.get().uri("/promociones")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PromotionResponse.class)
                .hasSize(1)
                .contains(promotionResponse);

        verify(getPromotionInputPort, times(1)).executeAll();
    }
}