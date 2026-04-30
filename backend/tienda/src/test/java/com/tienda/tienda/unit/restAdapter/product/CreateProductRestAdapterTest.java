package com.tienda.tienda.unit.restAdapter.product;

import com.tienda.tienda.product.application.port.input.CreateProductInputPort;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.CreateProductRestAdapter;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(CreateProductRestAdapter.class)
class CreateProductRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateProductInputPort createProductInputPort;

    @MockitoBean
    private ProductRestMapper productRestMapper;

    private Product basicProduct;
    private ProductResponse basicProductResponse;
    private ProductRequest basicProductRequest;

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

        basicProductRequest = new ProductRequest();
        basicProductRequest.setName("Camiseta");
        basicProductRequest.setPrice(20.0);
    }

    @Test
    void createProduct_shouldReturnCreatedProductAndStatus201() {
        when(productRestMapper.toDomain(any(ProductRequest.class))).thenReturn(basicProduct);
        when(createProductInputPort.execute(any(Product.class))).thenReturn(Mono.just(basicProduct));
        when(productRestMapper.toResponse(any(Product.class))).thenReturn(basicProductResponse);

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicProductRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .isEqualTo(basicProductResponse);

        verify(createProductInputPort, times(1)).execute(any(Product.class));
    }
}