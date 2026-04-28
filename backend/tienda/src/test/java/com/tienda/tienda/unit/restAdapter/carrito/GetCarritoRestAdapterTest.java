package com.tienda.tienda.unit.restAdapter.carrito;

import com.tienda.tienda.carrito.application.usecase.GetCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.GetCarritoRestAdapter;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.CarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
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
    private GetCarritoUseCase getCarritoUseCase;

    @MockitoBean
    private CarritoRestMapper carritoRestMapper;

    private Carrito basicCarrito;
    private CarritoResponse basicCarritoResponse;

    @BeforeEach
    void setUp() {
        basicCarrito = new Carrito();
        basicCarrito.setId(1);
        basicCarrito.setTotal(0.0);
        basicCarrito.setLineas(List.of());

        basicCarritoResponse = new CarritoResponse();
        basicCarritoResponse.setId(1);
        basicCarritoResponse.setTotal(0.0);
        basicCarritoResponse.setLineas(List.of());
    }

    @Test
    void getCarritoById_shouldReturnCarritoAndStatus200() {
        when(getCarritoUseCase.execute(1)).thenReturn(Mono.just(basicCarrito));
        when(carritoRestMapper.toResponse(basicCarrito)).thenReturn(basicCarritoResponse);

        webTestClient.get().uri("/carritos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoResponse.class)
                .isEqualTo(basicCarritoResponse);
    }

    @Test
    void getCarritoById_ifNotExists_shouldReturn404() {
        when(getCarritoUseCase.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/carritos/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}