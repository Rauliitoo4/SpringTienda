package com.tienda.productservice.unit.restAdapter;

import com.tienda.productservice.application.port.input.CreateProductInputPort;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.infrastructure.adapter.input.rest.CreateProductRestAdapter;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.request.ProductRequest;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.response.ProductResponse;
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

@WebFluxTest(CreateProductRestAdapter.class)
class CreateProductRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateProductInputPort createProductInputPort;

    @MockitoBean
    private ProductRestMapper mapper;

    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;

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

        productRequest = new ProductRequest();
        productRequest.setName("Camiseta");
        productRequest.setPrice(20.0);
    }

    @Test
    void createProduct_shouldReturnProductAndStatus201() {
        when(mapper.toDomain(any(ProductRequest.class))).thenReturn(product);
        when(createProductInputPort.execute(product)).thenReturn(Mono.just(product));
        when(mapper.toResponse(product)).thenReturn(productResponse);

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .isEqualTo(productResponse);

        verify(createProductInputPort, times(1)).execute(product);
    }
}