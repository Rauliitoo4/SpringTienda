package com.tienda.productservice.unit.restAdapter;

import com.tienda.productservice.application.port.input.RemovePromotionInputPort;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.infrastructure.adapter.input.rest.RemovePromotionRestAdapter;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.productservice.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(RemovePromotionRestAdapter.class)
class RemovePromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RemovePromotionInputPort removePromotionInputPort;

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
    void removePromotion_shouldReturnProductAndStatus200() {
        when(removePromotionInputPort.execute(1, 1)).thenReturn(Mono.just(product));
        when(mapper.toResponse(product)).thenReturn(productResponse);

        webTestClient.delete().uri("/productos/1/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(productResponse);

        verify(removePromotionInputPort, times(1)).execute(1, 1);
    }

    @Test
    void removePromotion_ifProductNotExists_shouldReturn404() {
        when(removePromotionInputPort.execute(99, 1)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/productos/99/promociones/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}