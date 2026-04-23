package com.tienda.tienda.unit.controller;

import com.tienda.tienda.carrito.application.dto.CarritoDTO;
import com.tienda.tienda.lineacarrito.application.dto.LineaCarritoDTO;
import com.tienda.tienda.product.application.dto.ProductDTO;
import com.tienda.tienda.carrito.application.service.CarritoService;
import com.tienda.tienda.carrito.infraestructure.controller.CarritoController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CarritoService carritoService;

    private CarritoDTO carritoEmpty;
    private CarritoDTO carritoWithLinea;

    @BeforeEach
    void setUp() {
        carritoEmpty = new CarritoDTO();
        carritoEmpty.setId(1);
        carritoEmpty.setTotal(0.0);
        carritoEmpty.setLineas(List.of());

        ProductDTO product = new ProductDTO();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(18.0);

        LineaCarritoDTO linea = new LineaCarritoDTO();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(36.0);
        linea.setProduct(product);

        carritoWithLinea = new CarritoDTO();
        carritoWithLinea.setId(1);
        carritoWithLinea.setTotal(36.0);
        carritoWithLinea.setLineas(List.of(linea));
    }

    //GET /carritos/{id}
    @Test
    void getCarritoById_shouldReturnCarritoAndStatus200() {
        when(carritoService.getCarritoById(1)).thenReturn(Mono.just(carritoEmpty));

        webTestClient.get().uri("/carritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoDTO.class)
                .isEqualTo(carritoEmpty);
    }

    @Test
    void getCarritoById_ifNotExists_shouldReturn404() {
        when(carritoService.getCarritoById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/carritos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /carritos/{carritoId}/productos/{productoId}
    @Test
    void addProductToCarrito_shouldReturnCarritoUpdatedAndStatus200() {
        when(carritoService.addProductToCarrito(1, 1, 2)).thenReturn(Mono.just(carritoWithLinea));

        webTestClient.post().uri("/carritos/1/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoDTO.class)
                .isEqualTo(carritoWithLinea);
    }

    @Test
    void addProductToCarrito_ifNotExistsCarrito_shouldReturn404() {
        when(carritoService.addProductToCarrito(99, 1, 2)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/99/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addProductToCarrito_ifNotExistsProduct_shouldReturn404() {
        when(carritoService.addProductToCarrito(1, 99, 2)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/1/productos/99?cantidad=2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addProductToCarrito_shouldCallService() {
        when(carritoService.addProductToCarrito(1, 1, 2)).thenReturn(Mono.just(carritoWithLinea));

        webTestClient.post().uri("/carritos/1/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isOk();

        verify(carritoService, times(1)).addProductToCarrito(1, 1, 2);
    }
}
