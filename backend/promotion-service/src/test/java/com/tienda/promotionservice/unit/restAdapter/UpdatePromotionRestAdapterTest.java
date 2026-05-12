package com.tienda.promotionservice.unit.restAdapter;

import com.tienda.promotionservice.application.port.input.UpdatePromotionInputPort;
import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.UpdatePromotionRestAdapter;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.promotion.model.PromotionRequest;
import com.tienda.promotion.model.PromotionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(UpdatePromotionRestAdapter.class)
class UpdatePromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UpdatePromotionInputPort updatePromotionInputPort;

    @MockitoBean
    private PromotionRestMapper mapper;

    private Promotion promotion;
    private PromotionResponse promotionResponse;
    private PromotionRequest promotionRequest;

    @BeforeEach
    void setUp() {
        promotion = new Promotion();
        promotion.setId(1);
        promotion.setDiscount(20.0);
        promotion.setDescription("Descuento actualizado");

        promotionResponse = new PromotionResponse();
        promotionResponse.setId(1);
        promotionResponse.setDiscount(20.0);
        promotionResponse.setDescription("Descuento actualizado");

        promotionRequest = new PromotionRequest();
        promotionRequest.setDiscount(20.0);
        promotionRequest.setDescription("Descuento actualizado");
    }

    @Test
    void updatePromotion_shouldReturnPromotionAndStatus200() {
        when(mapper.toDomain(any(PromotionRequest.class))).thenReturn(promotion);
        when(updatePromotionInputPort.execute(eq(1), any(Promotion.class))).thenReturn(Mono.just(promotion));
        when(mapper.toResponse(promotion)).thenReturn(promotionResponse);

        webTestClient.put().uri("/promociones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promotionRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PromotionResponse.class)
                .isEqualTo(promotionResponse);

        verify(updatePromotionInputPort, times(1)).execute(eq(1), any(Promotion.class));
    }

    @Test
    void updatePromotion_ifNotExists_shouldReturn404() {
        when(mapper.toDomain(any(PromotionRequest.class))).thenReturn(promotion);
        when(updatePromotionInputPort.execute(eq(99), any(Promotion.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/promociones/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promotionRequest)
                .exchange()
                .expectStatus().isNotFound();
    }
}