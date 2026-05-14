package com.tienda.productservice.unit.restAdapter;

import com.tienda.productservice.application.port.input.GetProductInputPort;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.infrastructure.adapter.input.rest.GetProductRestAdapter;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.product.model.ProductResponse;
import com.tienda.productservice.infrastructure.security.JwtAuthenticationFilter;
import com.tienda.productservice.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(GetProductRestAdapter.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class GetProductRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetProductInputPort getProductInputPort;

    @MockitoBean
    private ProductRestMapper mapper;

    private Product product;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);

        productResponse = new ProductResponse();
        productResponse.setId(1);
        productResponse.setName("Camiseta");
        productResponse.setPrice(20.0);
        productResponse.setFinalPrice(20.0);
    }

    @Test
    void getProductById_shouldReturnProductAndStatus200() {
        when(getProductInputPort.execute(1)).thenReturn(Mono.just(product));
        when(mapper.toResponse(product)).thenReturn(productResponse);

        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(productResponse);

        verify(getProductInputPort, times(1)).execute(1);
    }

    @Test
    void getProductById_ifNotExists_shouldReturn404() {
        when(getProductInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllProducts_shouldReturnListAndStatus200() {
        when(getProductInputPort.executeAll()).thenReturn(Flux.just(product));
        when(mapper.toResponse(product)).thenReturn(productResponse);

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(1)
                .contains(productResponse);

        verify(getProductInputPort, times(1)).executeAll();
    }
}