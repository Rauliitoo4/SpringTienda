package com.tienda.carritoservice.unit.restAdapter;

import com.tienda.carritoservice.application.port.input.AddProductToCarritoInputPort;
import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Size;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.AddProductToCarritoRestAdapter;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.AddProductToCarritoRequestMapper;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.CarritoRestMapper;
import com.tienda.carrito.model.AddProductToCarritoRequest;
import com.tienda.carrito.model.CarritoResponse;
import com.tienda.carrito.model.LineaCarritoResponse;
import com.tienda.carritoservice.infrastructure.security.JwtAuthenticationFilter;
import com.tienda.carritoservice.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(AddProductToCarritoRestAdapter.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class AddProductToCarritoRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AddProductToCarritoInputPort addProductToCarritoInputPort;

    @MockitoBean
    private CarritoRestMapper carritoRestMapper;

    @MockitoBean
    private AddProductToCarritoRequestMapper requestMapper;

    private Carrito carritoWithLinea;
    private CarritoResponse carritoWithLineaResponse;
    private AddProductToCarritoRequest request;

    @BeforeEach
    void setUp() {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(40.0);

        carritoWithLinea = new Carrito();
        carritoWithLinea.setId(1);
        carritoWithLinea.setTotal(40.0);
        carritoWithLinea.setLineas(List.of(linea));

        LineaCarritoResponse lineaResponse = new LineaCarritoResponse();
        lineaResponse.setId(1);
        lineaResponse.setQuantity(2);
        lineaResponse.setSubtotal(40.0);

        carritoWithLineaResponse = new CarritoResponse();
        carritoWithLineaResponse.setId(1);
        carritoWithLineaResponse.setTotal(40.0);
        carritoWithLineaResponse.setLineas(List.of(lineaResponse));

        request = new AddProductToCarritoRequest();
        request.setProductId(1);
        request.setQuantity(2);
        request.setSize(com.tienda.carrito.model.Size.M);
    }

    @Test
    @WithMockUser
    void addProductToCarrito_shouldReturnCarritoUpdatedAndStatus200() {
        when(requestMapper.toProductId(any(AddProductToCarritoRequest.class))).thenReturn(1);
        when(requestMapper.toQuantity(any(AddProductToCarritoRequest.class))).thenReturn(2);
        when(requestMapper.toSize(any(AddProductToCarritoRequest.class))).thenReturn(Size.M);
        when(addProductToCarritoInputPort.execute(1, 1, 2, Size.M)).thenReturn(Mono.just(carritoWithLinea));
        when(carritoRestMapper.toResponse(carritoWithLinea)).thenReturn(carritoWithLineaResponse);

        webTestClient.post().uri("/carritos/1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CarritoResponse.class)
                .isEqualTo(carritoWithLineaResponse);

        verify(addProductToCarritoInputPort, times(1)).execute(1, 1, 2, Size.M);
    }

    @Test
    @WithMockUser
    void addProductToCarrito_ifCarritoNotExists_shouldReturn404() {
        when(requestMapper.toProductId(any(AddProductToCarritoRequest.class))).thenReturn(1);
        when(requestMapper.toQuantity(any(AddProductToCarritoRequest.class))).thenReturn(2);
        when(requestMapper.toSize(any(AddProductToCarritoRequest.class))).thenReturn(Size.M);
        when(addProductToCarritoInputPort.execute(99, 1, 2, Size.M)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/99/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithMockUser
    void addProductToCarrito_ifProductNotExists_shouldReturn404() {
        AddProductToCarritoRequest requestProduct99 = new AddProductToCarritoRequest();
        requestProduct99.setProductId(99);
        requestProduct99.setQuantity(2);
        requestProduct99.setSize(com.tienda.carrito.model.Size.M);

        when(requestMapper.toProductId(any(AddProductToCarritoRequest.class))).thenReturn(99);
        when(requestMapper.toQuantity(any(AddProductToCarritoRequest.class))).thenReturn(2);
        when(requestMapper.toSize(any(AddProductToCarritoRequest.class))).thenReturn(Size.M);
        when(addProductToCarritoInputPort.execute(1, 99, 2, Size.M)).thenReturn(Mono.empty());

        webTestClient.post().uri("/carritos/1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestProduct99)
                .exchange()
                .expectStatus().isNotFound();
    }
}