package com.tienda.tienda.unit.controller;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.CarritoService;
import com.tienda.tienda.controller.CarritoController;

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

    private CarritoDTO carritoVacio;
    private CarritoDTO carritoConLinea;

    @BeforeEach
    void setUp() {
        carritoVacio = new CarritoDTO();
        carritoVacio.setId(1);
        carritoVacio.setTotal(0.0);
        carritoVacio.setLineas(List.of());

        ProductDTO producto = new ProductDTO();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(18.0);

        LineaCarritoDTO linea = new LineaCarritoDTO();
        linea.setId(1);
        linea.setCantidad(2);
        linea.setSubtotal(36.0);
        linea.setProducto(producto);

        carritoConLinea = new CarritoDTO();
        carritoConLinea.setId(1);
        carritoConLinea.setTotal(36.0);
        carritoConLinea.setLineas(List.of(linea));
    }

    //GET /carritos/{id}
    @Test
    void getCarritoById_existente_deberiaRetornarCarritoyStatus200() {
        when(carritoService.getCarritoById(1)).thenReturn(Mono.just(carritoVacio));

        webTestClient.get().uri("/carritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoDTO.class)
                .isEqualTo(carritoVacio);
    }

    @Test
    void getCarritoById_noExistente_deberiaRetornar404() {
        when(carritoService.getCarritoById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/carritos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /carritos/{carritoId}/productos/{productoId}
    @Test
    void addProductToCarrito_deberiaRetornarCarritoActualizadoYStatus200() {
        when(carritoService.addProductToCarrito(1, 1, 2)).thenReturn(Mono.just(carritoConLinea));

        webTestClient.post().uri("/carritos/1/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoDTO.class)
                .isEqualTo(carritoConLinea);
    }

    @Test
    void addProductToCarrito_carritoNoExiste_deberiaRetornar404() {
        when(carritoService.addProductToCarrito(99, 1, 2)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/99/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addProductToCarrito_productoNoExiste_deberiaRetornar404() {
        when(carritoService.addProductToCarrito(1, 99, 2)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/1/productos/99?cantidad=2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addProductToCarrito_deberiaDelegarEnServicio() {
        when(carritoService.addProductToCarrito(1, 1, 2)).thenReturn(Mono.just(carritoConLinea));

        webTestClient.post().uri("/carritos/1/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isOk();

        verify(carritoService, times(1)).addProductToCarrito(1, 1, 2);
    }
}
