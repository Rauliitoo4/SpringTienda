package com.tienda.carritoservice.unit.restAdapter;

import com.tienda.carritoservice.application.port.input.GetLineaCarritoInputPort;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Size;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.GetLineaCarritoRestAdapter;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.carrito.model.LineaCarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
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
    private LineaCarritoRestMapper mapper;

    private LineaCarrito linea;
    private LineaCarritoResponse lineaResponse;

    @BeforeEach
    void setUp() {
        linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(40.0);
        linea.setSize(Size.M);
        linea.setCarritoId(1);
        linea.setProductId(1);

        lineaResponse = new LineaCarritoResponse();
        lineaResponse.setId(1);
        lineaResponse.setQuantity(2);
        lineaResponse.setSubtotal(40.0);
        lineaResponse.setSize(com.tienda.carrito.model.Size.M);
        lineaResponse.setCarritoId(1);
        lineaResponse.setProductId(1);
    }

    @Test
    void getLineaById_shouldReturnLineaAndStatus200() {
        when(getLineaCarritoInputPort.execute(1)).thenReturn(Mono.just(linea));
        when(mapper.toResponse(linea)).thenReturn(lineaResponse);

        webTestClient.get().uri("/lineas/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineaCarritoResponse.class)
                .isEqualTo(lineaResponse);

        verify(getLineaCarritoInputPort, times(1)).execute(1);
    }

    @Test
    void getLineaById_ifNotExists_shouldReturn404() {
        when(getLineaCarritoInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/lineas/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllLineas_shouldReturnListAndStatus200() {
        when(getLineaCarritoInputPort.executeAll()).thenReturn(Flux.just(linea));
        when(mapper.toResponse(linea)).thenReturn(lineaResponse);

        webTestClient.get().uri("/lineas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LineaCarritoResponse.class)
                .hasSize(1)
                .contains(lineaResponse);

        verify(getLineaCarritoInputPort, times(1)).executeAll();
    }
}