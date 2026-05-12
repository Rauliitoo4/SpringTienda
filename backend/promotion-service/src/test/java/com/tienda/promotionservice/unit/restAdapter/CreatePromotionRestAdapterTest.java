package com.tienda.promotionservice.unit.restAdapter;

import com.tienda.promotionservice.application.port.input.CreatePromotionInputPort;
import com.tienda.promotionservice.domain.model.Promotion;
import com.tienda.promotionservice.infrastructure.adapter.input.rest.CreatePromotionRestAdapter;
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
import static org.mockito.Mockito.*;

@WebFluxTest(CreatePromotionRestAdapter.class)
class CreatePromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreatePromotionInputPort createPromotionInputPort;

    @MockitoBean
    private PromotionRestMapper mapper;

    private Promotion promotion;
    private PromotionResponse promotionResponse;
    private PromotionRequest promotionRequest;

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

        promotionRequest = new PromotionRequest();
        promotionRequest.setDiscount(10.0);
        promotionRequest.setDescription("Descuento verano");
    }

    @Test
    void createPromotion_shouldReturnPromotionAndStatus201() {
        when(mapper.toDomain(any(PromotionRequest.class))).thenReturn(promotion);
        when(createPromotionInputPort.execute(promotion)).thenReturn(Mono.just(promotion));
        when(mapper.toResponse(promotion)).thenReturn(promotionResponse);

        webTestClient.post().uri("/promociones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promotionRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PromotionResponse.class)
                .isEqualTo(promotionResponse);

        verify(createPromotionInputPort, times(1)).execute(promotion);
    }
}