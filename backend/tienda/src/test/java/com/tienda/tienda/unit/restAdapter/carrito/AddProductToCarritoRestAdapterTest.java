package com.tienda.tienda.unit.restAdapter.carrito;

import com.tienda.tienda.carrito.application.usecase.AddProductToCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.AddProductToCarritoRestAdapter;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(AddProductToCarritoRestAdapter.class)
class AddProductToCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AddProductToCarritoUseCase addProductToCarritoUseCase;

    @MockitoBean
    private CarritoRestMapper carritoRestMapper;

    private Carrito carritoWithLinea;
    private CarritoResponse carritoWithLineaResponse;

    @BeforeEach
    void setUp() {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(36.0);

        carritoWithLinea = new Carrito();
        carritoWithLinea.setId(1);
        carritoWithLinea.setTotal(36.0);
        carritoWithLinea.setLineas(List.of(linea));

        LineaCarritoResponse lineaResponse = new LineaCarritoResponse();
        lineaResponse.setId(1);
        lineaResponse.setQuantity(2);
        lineaResponse.setSubtotal(36.0);

        carritoWithLineaResponse = new CarritoResponse();
        carritoWithLineaResponse.setId(1);
        carritoWithLineaResponse.setTotal(36.0);
        carritoWithLineaResponse.setLineas(List.of(lineaResponse));
    }

    @Test
    void addProductToCarrito_shouldReturnCarritoUpdatedAndStatus200() {
        when(addProductToCarritoUseCase.execute(1, 1, 2)).thenReturn(Mono.just(carritoWithLinea));
        when(carritoRestMapper.toResponse(carritoWithLinea)).thenReturn(carritoWithLineaResponse);

        webTestClient.post().uri("/carritos/1/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoResponse.class)
                .isEqualTo(carritoWithLineaResponse);

        verify(addProductToCarritoUseCase, times(1)).execute(1, 1, 2);
    }

    @Test
    void addProductToCarrito_ifNotExistsCarrito_shouldReturn404() {
        when(addProductToCarritoUseCase.execute(99, 1, 2)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/99/productos/1?cantidad=2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addProductToCarrito_ifNotExistsProduct_shouldReturn404() {
        when(addProductToCarritoUseCase.execute(1, 99, 2)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/1/productos/99?cantidad=2")
                .exchange()
                .expectStatus().isNotFound();
    }
}