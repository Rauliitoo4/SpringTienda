package com.tienda.tienda.unit.controller;

import com.tienda.tienda.lineacarrito.application.dto.LineaCarritoDTO;
import com.tienda.tienda.product.application.dto.ProductDTO;
import com.tienda.tienda.lineacarrito.application.service.LineaCarritoService;
import com.tienda.tienda.lineacarrito.infraestructure.controller.LineaCarritoController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(LineaCarritoController.class)
class LineaCarritoControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LineaCarritoService lineaCarritoService;

    private LineaCarritoDTO linea;

    @BeforeEach
    void setUp() {
        ProductDTO product = new ProductDTO();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(18.0);

        linea = new LineaCarritoDTO();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(36.0);
        linea.setCarritoId(1);
        linea.setProduct(product);

    }

    //GET /lineas
    @Test
    void getAllLineas_shouldReturnListAndStatus200() {
        when(lineaCarritoService.getAllLineas()).thenReturn(Flux.just(linea));

        webTestClient.get().uri("/lineas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LineaCarritoDTO.class)
                .hasSize(1);
    }

    @Test
    void getAllLineas_EmptyList_shouldReturnEmptyArrayAndStatus200() {
        when(lineaCarritoService.getAllLineas()).thenReturn(Flux.empty());

        webTestClient.get().uri("/lineas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LineaCarritoDTO.class)
                .hasSize(0);
    }

    //GET /lineas/{id}
    @Test
    void getLineaById_shouldReturnLineaAndStatus200() {
        when(lineaCarritoService.getLineaById(1)).thenReturn(Mono.just(linea));

        webTestClient.get().uri("/lineas/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineaCarritoDTO.class)
                .isEqualTo(linea);
    }

    @Test
    void getLineaById_ifNotExists_shouldReturn404() {
        when(lineaCarritoService.getLineaById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/lineas/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //PUT /lineas/{id}
    @Test
    void updateLinea_shouldReturnLineaUpdatedAndStatus200() {
        linea.setQuantity(5);
        linea.setSubtotal(90.0);
        when(lineaCarritoService.updateLinea(eq(1), eq(5))).thenReturn(Mono.just(linea));

        webTestClient.put().uri("/lineas/1?cantidad=5")
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineaCarritoDTO.class)
                .isEqualTo(linea);
    }

    @Test
    void updateLinea_ifNotExists_shouldReturn404() {
        when(lineaCarritoService.updateLinea(eq(99), eq(5))).thenReturn(Mono.empty());

        webTestClient.put().uri("/lineas/99?cantidad=5")
                .exchange()
                .expectStatus().isNotFound();
    }

    //DELETE /lineas/{id}
    @Test
    void deleteLinea_shouldReturn204() {
        when(lineaCarritoService.deleteLinea(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/lineas/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteLinea_ifNotExists_shouldReturn404() {
        when(lineaCarritoService.deleteLinea(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/lineas/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}
