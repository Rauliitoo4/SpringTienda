package com.tienda.tienda.unit.restAdapter.product;

import com.tienda.tienda.product.application.usecase.AddPromotionUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.AddPromotionRestAdapter;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.response.ProductResponse;
import com.tienda.tienda.promotion.infrastructure.adapter.input.rest.data.response.PromotionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(AddPromotionRestAdapter.class)
class AddPromotionRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AddPromotionUseCase addPromotionUseCase;

    @MockitoBean
    private ProductRestMapper productRestMapper;

    private Product basicProduct;
    private ProductResponse productWithPromo;

    @BeforeEach
    void setUp() {
        basicProduct = new Product();
        basicProduct.setId(1);
        basicProduct.setName("Camiseta");
        basicProduct.setPrice(20.0);
        basicProduct.setFinalPrice(18.0);

        PromotionResponse promo = new PromotionResponse();
        promo.setId(1);
        promo.setDescription("10% de descuento");
        promo.setDiscount(10.0);

        productWithPromo = new ProductResponse();
        productWithPromo.setId(1);
        productWithPromo.setName("Camiseta");
        productWithPromo.setPrice(20.0);
        productWithPromo.setFinalPrice(18.0);
        productWithPromo.setPromotions(List.of(promo));
    }

    @Test
    void addPromotion_shouldReturnProductWithPromoAndStatus200() {
        when(addPromotionUseCase.execute(1, 1)).thenReturn(Mono.just(basicProduct));
        when(productRestMapper.toResponse(basicProduct)).thenReturn(productWithPromo);

        webTestClient.post().uri("/productos/1/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(productWithPromo);
    }

    @Test
    void addPromotion_ifNotExistsProduct_shouldReturn404() {
        when(addPromotionUseCase.execute(99, 1)).thenReturn(Mono.empty());

        webTestClient.post().uri("/productos/99/promociones/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addPromotion_ifNotExistsPromotion_shouldReturn404() {
        when(addPromotionUseCase.execute(1, 99)).thenReturn(Mono.empty());

        webTestClient.post().uri("/productos/1/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }
}