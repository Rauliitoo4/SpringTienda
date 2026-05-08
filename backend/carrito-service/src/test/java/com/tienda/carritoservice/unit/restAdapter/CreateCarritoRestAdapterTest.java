package com.tienda.carritoservice.unit.restAdapter;

import com.tienda.carritoservice.application.port.input.CreateCarritoInputPort;
import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.CreateCarritoRestAdapter;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(CreateCarritoRestAdapter.class)
class CreateCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateCarritoInputPort createCarritoInputPort;

    @MockitoBean
    private CarritoRestMapper carritoRestMapper;

    private Carrito carrito;
    private CarritoResponse carritoResponse;

    @BeforeEach
    void setUp() {
        carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);

        carritoResponse = new CarritoResponse();
        carritoResponse.setId(1);
        carritoResponse.setTotal(0.0);
    }

    @Test
    void createCarrito_shouldReturnCarritoAndStatus201() {
        when(createCarritoInputPort.execute()).thenReturn(Mono.just(carrito));
        when(carritoRestMapper.toResponse(carrito)).thenReturn(carritoResponse);

        webTestClient.post().uri("/carritos")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CarritoResponse.class)
                .isEqualTo(carritoResponse);

        verify(createCarritoInputPort, times(1)).execute();
    }
}