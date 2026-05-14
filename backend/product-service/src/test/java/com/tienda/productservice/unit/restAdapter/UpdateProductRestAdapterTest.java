package com.tienda.productservice.unit.restAdapter;

import com.tienda.productservice.application.port.input.UpdateProductInputPort;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.infrastructure.adapter.input.rest.UpdateProductRestAdapter;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.product.model.ProductRequest;
import com.tienda.product.model.ProductResponse;
import com.tienda.productservice.infrastructure.security.JwtAuthenticationFilter;
import com.tienda.productservice.infrastructure.security.SecurityConfig;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(UpdateProductRestAdapter.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class UpdateProductRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UpdateProductInputPort updateProductInputPort;

    @MockitoBean
    private ProductRestMapper mapper;

    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Camiseta Actualizada");
        product.setPrice(25.0);
        product.setFinalPrice(25.0);

        productResponse = new ProductResponse();
        productResponse.setId(1);
        productResponse.setName("Camiseta Actualizada");
        productResponse.setPrice(25.0);
        productResponse.setFinalPrice(25.0);

        productRequest = new ProductRequest();
        productRequest.setName("Camiseta Actualizada");
        productRequest.setPrice(25.0);
    }

    @Test
    @WithMockUser
    void updateProduct_shouldReturnProductAndStatus200() {
        when(mapper.toDomain(any(ProductRequest.class))).thenReturn(product);
        when(updateProductInputPort.execute(eq(1), any(Product.class))).thenReturn(Mono.just(product));
        when(mapper.toResponse(product)).thenReturn(productResponse);

        webTestClient.put().uri("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(productResponse);

        verify(updateProductInputPort, times(1)).execute(eq(1), any(Product.class));
    }

    @Test
    @WithMockUser
    void updateProduct_ifNotExists_shouldReturn404() {
        when(mapper.toDomain(any(ProductRequest.class))).thenReturn(product);
        when(updateProductInputPort.execute(eq(99), any(Product.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/productos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productRequest)
                .exchange()
                .expectStatus().isNotFound();
    }
}