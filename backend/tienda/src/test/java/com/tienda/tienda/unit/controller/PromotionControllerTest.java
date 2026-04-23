package com.tienda.tienda.unit.controller;

import com.tienda.tienda.promotion.application.dto.PromotionDTO;
import com.tienda.tienda.promotion.application.service.PromotionService;
import com.tienda.tienda.promotion.infraestructure.controller.PromotionController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(PromotionController.class)
class PromotionControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PromotionService promotionService;

    private PromotionDTO promo;

    @BeforeEach
    void setUp() {
        promo = new PromotionDTO();
        promo.setId(1);
        promo.setDescription("Rebajas de verano");
        promo.setDiscount(10.0);
    }

    //GET /promociones
    @Test
    void getAllPromotions_shouldReturnListAndStatus200() {
        when(promotionService.getAllPromotions()).thenReturn(Flux.just(promo));

        webTestClient.get().uri("/promociones")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PromotionDTO.class)
                .hasSize(1);

    }

    @Test
    void getAllPromotions_emptyList_shouldReturnEmptyArrayAndStatus200() {
        when(promotionService.getAllPromotions()).thenReturn(Flux.empty());

        webTestClient.get().uri("/promociones")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PromotionDTO.class)
                .hasSize(0);
    }

    //GET /promociones/{id}
    @Test
    void getPromotionById_shouldReturnPromotionAndStatus200() {
        when(promotionService.getPromotionById(1)).thenReturn(Mono.just(promo));

        webTestClient.get().uri("/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PromotionDTO.class)
                .isEqualTo(promo);
    }

    @Test
    void getPromotionById_ifNotExists_shouldReturn404() {
        when(promotionService.getPromotionById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /promociones
    @Test
    void createPromotion_shouldReturnCreatedPromotionAndStatus201() {
        when(promotionService.createPromotion(any(PromotionDTO.class))).thenReturn(Mono.just(promo));

        webTestClient.post().uri("/promociones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promo)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PromotionDTO.class)
                .isEqualTo(promo);
    }

    @Test
    void createPromotion_shouldCallService() {
        when(promotionService.createPromotion(any(PromotionDTO.class))).thenReturn(Mono.just(promo));

        webTestClient.post().uri("/promociones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promo)
                .exchange()
                .expectStatus().isCreated();
        verify(promotionService, times(1)).createPromotion(any(PromotionDTO.class));
    }

    //PUT /promociones/{id}
    @Test
    void updatePromotion_shouldReturnUpdatedPromotionAndStatus200() {
        promo.setDiscount(50.0);
        when(promotionService.updatePromotion(eq(1), any(PromotionDTO.class))).thenReturn(Mono.just(promo));

        webTestClient.put().uri("/promociones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promo)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PromotionDTO.class)
                .isEqualTo(promo);
    }

    @Test
    void updatePromotion_ifNotExists_shouldReturn404() {
        when(promotionService.updatePromotion(eq(99), any(PromotionDTO.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/promociones/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(promo)
                .exchange()
                .expectStatus().isNotFound();
    }
    //DELETE /promociones/{id}
    @Test
    void deletePromotion_shouldReturn204() {
        when(promotionService.deletePromotion(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/promociones/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deletePromotion_ifNotExists_shouldReturn404() {
        when(promotionService.deletePromotion(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}
