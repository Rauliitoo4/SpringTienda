package com.tienda.tienda.unit.controller;

import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.promotion.application.dto.PromotionResponse;
import com.tienda.tienda.product.application.service.ProductService;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.ProductController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(ProductController.class)
class ProductEntityControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductService productService;

    private ProductResponse basicProduct;
    private ProductResponse productWithPromo;

    @BeforeEach
    void setUp() {
        basicProduct = new ProductResponse();
        basicProduct.setId(1);
        basicProduct.setName("Camiseta");
        basicProduct.setPrice(20.0);
        basicProduct.setFinalPrice(20.0);
        basicProduct.setDescription("Camiseta de algodón");
        basicProduct.setMaterial("Algodón");
        basicProduct.setConsiderations("Lavar a mano");
        basicProduct.setImageUrl("http://img.com/camiseta.jpg");
        basicProduct.setPromotions(List.of());

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

    //GET /productos
    @Test
    void getAllProdcuts_shouldReturnListAndStatus200() {
        when(productService.getAllProducts()).thenReturn(Flux.just(basicProduct));

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(1)
                .contains(basicProduct);
    }

    @Test
    void getAllProducts_EmptyList_shouldReturnEmptyArraAndStatus200() {
        when(productService.getAllProducts()).thenReturn(Flux.empty());

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(0);
    }

    //GET /productos/{id}
    @Test
    void getProductById_shouldReturnProductAndStatus200() {
        when(productService.getProductById(1)).thenReturn(Mono.just(basicProduct));

        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(basicProduct);
    }

    @Test
    void getProductById_ifNotExists_shouldReturn404() {
        when(productService.getProductById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /productos
    @Test
    void createProduct_shouldReturnCreatedProductAndStatus201() {
        when(productService.createProduct(any(ProductResponse.class))).thenReturn(Mono.just(basicProduct));

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicProduct)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .isEqualTo(basicProduct);
    }

    @Test
    void createProduct_shouldCallService() {
        when(productService.createProduct(any(ProductResponse.class))).thenReturn(Mono.just(basicProduct));

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicProduct)
                .exchange()
                .expectStatus().isCreated();

        verify(productService, times(1)).createProduct(any(ProductResponse.class));
    }

    //PUT /productos/{id}
    @Test
    void updateProduct_shouldReturnUpdatedProductAndStatus200() {
        basicProduct.setName("Camiseta Actualizada");
        when(productService.updateProduct(eq(1), any(ProductResponse.class))).thenReturn(Mono.just(basicProduct));

        webTestClient.put().uri("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicProduct)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(basicProduct);
    }

    @Test
    void updateProduct_ifNotExists_shouldReturn404() {
        when(productService.updateProduct(eq(99), any(ProductResponse.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/productos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicProduct)
                .exchange()
                .expectStatus().isNotFound();
    }

    //DELETE /productos/{id}
    @Test
    void deleteProduct_shouldReturn204() {
        when(productService.deleteProduct(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/productos/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteProduct_ifNotExists_shouldReturn404() {
        when(productService.deleteProduct(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /productos/{productoID}/promociones/{promocionID}
    @Test
    void addPromotion_shouldReturnProductWithPromoAndStatus200() {
        when(productService.addPromotion(1, 1)).thenReturn(Mono.just(productWithPromo));

        webTestClient.post().uri("/productos/1/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(productWithPromo);
    }

    @Test
    void addPromotion_ifNotExistsProduct_shouldReturn404() {
        when(productService.addPromotion(99, 1)).thenReturn(Mono.empty());

        webTestClient.post().uri("/productos/99/promociones/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addPromotion_ifNotExistsPromotion_shouldReturn404() {
        when(productService.addPromotion(1, 99)).thenReturn(Mono.empty());

        webTestClient.post().uri("/productos/1/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //DELETE /productos/{productoID}/promociones/{promocionID}
    @Test
    void removePromotion_shouldReturnProductWithOutPromoAndStatus200() {
        when(productService.removePromotion(1, 1)).thenReturn(Mono.just(basicProduct));

        webTestClient.delete().uri("/productos/1/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(basicProduct);
    }

    @Test
    void removePromotion_ifNotExistsProduct_shouldReturn404() {
        when(productService.removePromotion(99, 1)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/productos/99/promociones/1")
                .exchange()
                .expectStatus().isNotFound();
    }

}
