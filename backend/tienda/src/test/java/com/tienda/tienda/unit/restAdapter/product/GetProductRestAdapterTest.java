package com.tienda.tienda.unit.restAdapter.product;

import com.tienda.tienda.product.application.port.input.GetProductInputPort;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.GetProductRestAdapter;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(GetProductRestAdapter.class)
class GetProductRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetProductInputPort getProductInputPort;

    @MockitoBean
    private ProductRestMapper productRestMapper;

    private Product basicProduct;
    private ProductResponse basicProductResponse;

    @BeforeEach
    void setUp() {
        basicProduct = new Product();
        basicProduct.setId(1);
        basicProduct.setName("Camiseta");
        basicProduct.setPrice(20.0);
        basicProduct.setFinalPrice(20.0);
        basicProduct.setPromotions(List.of());

        basicProductResponse = new ProductResponse();
        basicProductResponse.setId(1);
        basicProductResponse.setName("Camiseta");
        basicProductResponse.setPrice(20.0);
        basicProductResponse.setFinalPrice(20.0);
        basicProductResponse.setPromotions(List.of());
    }

    @Test
    void getAllProducts_shouldReturnListAndStatus200() {
        when(getProductInputPort.executeAll()).thenReturn(Flux.just(basicProduct));
        when(productRestMapper.toResponse(basicProduct)).thenReturn(basicProductResponse);

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(1);
    }

    @Test
    void getAllProducts_emptyList_shouldReturnEmptyArrayAndStatus200() {
        when(getProductInputPort.executeAll()).thenReturn(Flux.empty());

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(0);
    }

    @Test
    void getProductById_shouldReturnProductAndStatus200() {
        when(getProductInputPort.execute(1)).thenReturn(Mono.just(basicProduct));
        when(productRestMapper.toResponse(basicProduct)).thenReturn(basicProductResponse);

        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(basicProductResponse);
    }

    @Test
    void getProductById_ifNotExists_shouldReturn404() {
        when(getProductInputPort.execute(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}