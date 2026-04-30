package com.tienda.tienda.unit.restAdapter.promotion;

import com.tienda.tienda.promotion.application.port.input.CreatePromotionInputPort;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.CreatePromotionRestAdapter;
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
import static org.mockito.Mockito.*;

@WebFluxTest(CreatePromotionRestAdapter.class)
class CreatePromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreatePromotionInputPort createPromotionInputPort;

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
        basicPromotionResponse.setDiscount(10.0);

        basicPromotionRequest = new PromotionRequest();
        basicPromotionRequest.setDescription("Rebajas de verano");
        basicPromotionRequest.setDiscount(10.0);
    }

    @Test
    void createPromotion_shouldReturnCreatedPromotionAndStatus201() {
        when(promotionRestMapper.toDomain(any(PromotionRequest.class))).thenReturn(basicPromotion);
        when(createPromotionInputPort.execute(any(Promotion.class))).thenReturn(Mono.just(basicPromotion));
        when(promotionRestMapper.toResponse(any(Promotion.class))).thenReturn(basicPromotionResponse);

        webTestClient.post().uri("/promociones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicPromotionRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PromotionResponse.class)
                .isEqualTo(basicPromotionResponse);

        verify(createPromotionInputPort, times(1)).execute(any(Promotion.class));
    }
}