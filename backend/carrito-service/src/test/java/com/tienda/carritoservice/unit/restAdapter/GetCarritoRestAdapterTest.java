package com.tienda.carritoservice.unit.restAdapter;

import com.tienda.carritoservice.application.port.input.GetCarritoInputPort;
import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.GetCarritoRestAdapter;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.carrito.model.CarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(GetCarritoRestAdapter.class)
class GetCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetCarritoInputPort getCarritoInputPort;

    @MockitoBean
    private CarritoRestMapper mapper;

    private Carrito carrito;
    private CarritoResponse carritoResponse;

    @BeforeEach
    void setUp() {
        carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(40.0);
        carrito.setLineas(List.of());

        carritoResponse = new CarritoResponse();
        carritoResponse.setId(1);
        carritoResponse.setTotal(40.0);
        carritoResponse.setLineas(List.of());
    }

    @Test
    void getCarrito_shouldReturnCarritoAndStatus200() {
        when(getCarritoInputPort.execute(1)).thenReturn(Mono.just(carrito));
        when(mapper.toResponse(carrito)).thenReturn(carritoResponse);

        webTestClient.get().uri("/carritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoResponse.class)
                .isEqualTo(carritoResponse);

        verify(getCarritoInputPort, times(1)).execute(1);
    }

    @Test
    void getCarrito_ifNotExists_shouldReturn404() {
        when(getCarritoInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/carritos/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}