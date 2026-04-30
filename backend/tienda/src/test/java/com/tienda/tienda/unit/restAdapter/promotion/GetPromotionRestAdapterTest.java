package com.tienda.tienda.unit.restAdapter.promotion;

import com.tienda.tienda.promotion.application.port.input.GetPromotionInputPort;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.GetPromotionRestAdapter;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
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
    private PromotionRestMapper promotionRestMapper;

    private Promotion basicPromotion;
    private PromotionResponse basicPromotionResponse;

    @BeforeEach
    void setUp() {
        basicPromotion = new Promotion();
        basicPromotion.setId(1);
        basicPromotion.setDescription("Rebajas de verano");
        basicPromotion.setDiscount(10.0);

        basicPromotionResponse = new PromotionResponse();
        basicPromotionResponse.setId(1);
        basicPromotionResponse.setDescription("Rebajas de verano");
        basicPromotionResponse.setDiscount(10.0);
    }

    @Test
    void getAllPromotions_shouldReturnListAndStatus200() {
        when(getPromotionInputPort.executeAll()).thenReturn(Flux.just(basicPromotion));
        when(promotionRestMapper.toResponse(basicPromotion)).thenReturn(basicPromotionResponse);

        webTestClient.get().uri("/promociones")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PromotionResponse.class)
                .hasSize(1);
    }

    @Test
    void getAllPromotions_emptyList_shouldReturnEmptyArrayAndStatus200() {
        when(getPromotionInputPort.executeAll()).thenReturn(Flux.empty());

        webTestClient.get().uri("/promociones")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PromotionResponse.class)
                .hasSize(0);
    }

    @Test
    void getPromotionById_shouldReturnPromotionAndStatus200() {
        when(getPromotionInputPort.execute(1)).thenReturn(Mono.just(basicPromotion));
        when(promotionRestMapper.toResponse(basicPromotion)).thenReturn(basicPromotionResponse);

        webTestClient.get().uri("/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PromotionResponse.class)
                .isEqualTo(basicPromotionResponse);
    }

    @Test
    void getPromotionById_ifNotExists_shouldReturn404() {
        when(getPromotionInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}