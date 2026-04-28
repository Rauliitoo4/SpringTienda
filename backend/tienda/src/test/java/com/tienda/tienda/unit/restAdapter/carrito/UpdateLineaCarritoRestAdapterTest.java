package com.tienda.tienda.unit.restAdapter.carrito;

import com.tienda.tienda.carrito.application.usecase.UpdateLineaCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.UpdateLineaCarritoRestAdapter;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(UpdateLineaCarritoRestAdapter.class)
class UpdateLineaCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UpdateLineaCarritoUseCase updateLineaCarritoUseCase;

    @MockitoBean
    private LineaCarritoRestMapper lineaCarritoRestMapper;

    private LineaCarrito basicLinea;
    private LineaCarritoResponse basicLineaResponse;

    @BeforeEach
    void setUp() {
        basicLinea = new LineaCarrito();
        basicLinea.setId(1);
        basicLinea.setQuantity(5);
        basicLinea.setSubtotal(90.0);
        basicLinea.setCarritoId(1);

        basicLineaResponse = new LineaCarritoResponse();
        basicLineaResponse.setId(1);
        basicLineaResponse.setQuantity(5);
        basicLineaResponse.setSubtotal(90.0);
        basicLineaResponse.setCarritoId(1);
    }

    @Test
    void updateLinea_shouldReturnLineaUpdatedAndStatus200() {
        when(updateLineaCarritoUseCase.execute(eq(1), eq(5))).thenReturn(Mono.just(basicLinea));
        when(lineaCarritoRestMapper.toResponse(basicLinea)).thenReturn(basicLineaResponse);

        webTestClient.put().uri("/lineas/1?cantidad=5")
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineaCarritoResponse.class)
                .isEqualTo(basicLineaResponse);
    }

    @Test
    void updateLinea_ifNotExists_shouldReturn404() {
        when(updateLineaCarritoUseCase.execute(eq(99), eq(5))).thenReturn(Mono.empty());

        webTestClient.put().uri("/lineas/99?cantidad=5")
                .exchange()
                .expectStatus().isNotFound();
    }
}