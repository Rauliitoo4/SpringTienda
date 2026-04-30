package com.tienda.tienda.unit.restAdapter.carrito;

import com.tienda.tienda.carrito.application.port.input.GetLineaCarritoInputPort;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.GetLineaCarritoRestAdapter;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(GetLineaCarritoRestAdapter.class)
class GetLineaCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetLineaCarritoInputPort getLineaCarritoInputPort;

    @MockitoBean
    private LineaCarritoRestMapper lineaCarritoRestMapper;

    private LineaCarrito basicLinea;
    private LineaCarritoResponse basicLineaResponse;

    @BeforeEach
    void setUp() {
        basicLinea = new LineaCarrito();
        basicLinea.setId(1);
        basicLinea.setQuantity(2);
        basicLinea.setSubtotal(36.0);
        basicLinea.setCarritoId(1);

        basicLineaResponse = new LineaCarritoResponse();
        basicLineaResponse.setId(1);
        basicLineaResponse.setQuantity(2);
        basicLineaResponse.setSubtotal(36.0);
        basicLineaResponse.setCarritoId(1);
    }

    @Test
    void getAllLineas_shouldReturnListAndStatus200() {
        when(getLineaCarritoInputPort.executeAll()).thenReturn(Flux.just(basicLinea));
        when(lineaCarritoRestMapper.toResponse(basicLinea)).thenReturn(basicLineaResponse);

        webTestClient.get().uri("/lineas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LineaCarritoResponse.class)
                .hasSize(1);
    }

    @Test
    void getAllLineas_emptyList_shouldReturnEmptyArrayAndStatus200() {
        when(getLineaCarritoInputPort.executeAll()).thenReturn(Flux.empty());

        webTestClient.get().uri("/lineas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LineaCarritoResponse.class)
                .hasSize(0);
    }

    @Test
    void getLineaById_shouldReturnLineaAndStatus200() {
        when(getLineaCarritoInputPort.execute(1)).thenReturn(Mono.just(basicLinea));
        when(lineaCarritoRestMapper.toResponse(basicLinea)).thenReturn(basicLineaResponse);

        webTestClient.get().uri("/lineas/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineaCarritoResponse.class)
                .isEqualTo(basicLineaResponse);
    }

    @Test
    void getLineaById_ifNotExists_shouldReturn404() {
        when(getLineaCarritoInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/lineas/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}