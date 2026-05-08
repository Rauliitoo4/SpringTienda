package com.tienda.carritoservice.unit.restAdapter;

import com.tienda.carritoservice.application.port.input.UpdateLineaCarritoInputPort;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Size;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.UpdateLineaCarritoRestAdapter;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.UpdateLineaCarritoRequestMapper;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.request.UpdateLineaCarritoRequest;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(UpdateLineaCarritoRestAdapter.class)
class UpdateLineaCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UpdateLineaCarritoInputPort updateLineaCarritoInputPort;

    @MockitoBean
    private LineaCarritoRestMapper lineaCarritoRestMapper;

    @MockitoBean
    private UpdateLineaCarritoRequestMapper requestMapper;

    private LineaCarrito linea;
    private LineaCarritoResponse lineaResponse;
    private UpdateLineaCarritoRequest request;

    @BeforeEach
    void setUp() {
        linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(5);
        linea.setSubtotal(100.0);
        linea.setSize(Size.M);
        linea.setCarritoId(1);
        linea.setProductId(1);

        lineaResponse = new LineaCarritoResponse();
        lineaResponse.setId(1);
        lineaResponse.setQuantity(5);
        lineaResponse.setSubtotal(100.0);
        lineaResponse.setSize(Size.M);
        lineaResponse.setCarritoId(1);
        lineaResponse.setProductId(1);

        request = new UpdateLineaCarritoRequest();
        request.setQuantity(5);
    }

    @Test
    void updateLinea_shouldReturnLineaUpdatedAndStatus200() {
        when(requestMapper.toQuantity(any(UpdateLineaCarritoRequest.class))).thenReturn(5);
        when(updateLineaCarritoInputPort.execute(1, 5)).thenReturn(Mono.just(linea));
        when(lineaCarritoRestMapper.toResponse(linea)).thenReturn(lineaResponse);

        webTestClient.put().uri("/lineas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineaCarritoResponse.class)
                .isEqualTo(lineaResponse);

        verify(updateLineaCarritoInputPort, times(1)).execute(1, 5);
    }

    @Test
    void updateLinea_ifNotExists_shouldReturn404() {
        when(requestMapper.toQuantity(any(UpdateLineaCarritoRequest.class))).thenReturn(5);
        when(updateLineaCarritoInputPort.execute(99, 5)).thenReturn(Mono.empty());

        webTestClient.put().uri("/lineas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }
}