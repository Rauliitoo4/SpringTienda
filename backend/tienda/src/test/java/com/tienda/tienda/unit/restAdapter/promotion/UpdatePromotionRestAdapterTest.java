package com.tienda.tienda.unit.restAdapter.promotion;

import com.tienda.tienda.promotion.application.port.input.UpdatePromotionInputPort;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.UpdatePromotionRestAdapter;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.mapper.PromotionRestMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.request.PromotionRequest;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
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
    private PromotionRestMapper promotionRestMapper;

    private Promotion basicPromotion;
    private PromotionResponse basicPromotionResponse;
    private PromotionRequest basicPromotionRequest;

    @BeforeEach
    void setUp() {
        basicPromotion = new Promotion();
        basicPromotion.setId(1);
        basicPromotion.setDescription("Rebajas de verano");
        basicPromotion.setDiscount(10.0);

        basicPromotionResponse = new PromotionResponse();
        basicPromotionResponse.setId(1);
        basicPromotionResponse.setDescription("Rebajas de verano");
        basicPromotionResponse.setDiscount(50.0);

        basicPromotionRequest = new PromotionRequest();
        basicPromotionRequest.setDiscount(50.0);
    }

    @Test
    void updatePromotion_shouldReturnUpdatedPromotionAndStatus200() {
        when(promotionRestMapper.toDomain(any(PromotionRequest.class))).thenReturn(basicPromotion);
        when(updatePromotionInputPort.execute(eq(1), any(Promotion.class))).thenReturn(Mono.just(basicPromotion));
        when(promotionRestMapper.toResponse(any(Promotion.class))).thenReturn(basicPromotionResponse);

        webTestClient.put().uri("/promociones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicPromotionRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PromotionResponse.class)
                .isEqualTo(basicPromotionResponse);
    }

    @Test
    void updatePromotion_ifNotExists_shouldReturn404() {
        when(promotionRestMapper.toDomain(any(PromotionRequest.class))).thenReturn(basicPromotion);
        when(updatePromotionInputPort.execute(eq(99), any(Promotion.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/promociones/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicPromotionRequest)
                .exchange()
                .expectStatus().isNotFound();
    }
}