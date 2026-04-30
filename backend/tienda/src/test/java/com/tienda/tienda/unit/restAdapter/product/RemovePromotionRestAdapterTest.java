package com.tienda.tienda.unit.restAdapter.product;

import com.tienda.tienda.product.application.port.input.RemovePromotionInputPort;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.RemovePromotionRestAdapter;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(RemovePromotionRestAdapter.class)
class RemovePromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RemovePromotionInputPort removePromotionInputPort;

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
    void removePromotion_shouldReturnProductWithoutPromoAndStatus200() {
        when(removePromotionInputPort.execute(1, 1)).thenReturn(Mono.just(basicProduct));
        when(productRestMapper.toResponse(basicProduct)).thenReturn(basicProductResponse);

        webTestClient.delete().uri("/productos/1/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(basicProductResponse);
    }

    @Test
    void removePromotion_ifNotExistsProduct_shouldReturn404() {
        when(removePromotionInputPort.execute(99, 1)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/productos/99/promociones/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}
