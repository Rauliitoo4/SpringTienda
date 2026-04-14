package com.tienda.tienda.unit.controller;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.service.ProductService;
import com.tienda.tienda.controller.ProductController;

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
class ProductControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductService productService;

    private ProductDTO productoBase;
    private ProductDTO productoConPromo;

    @BeforeEach
    void setUp() {
        productoBase = new ProductDTO();
        productoBase.setId(1);
        productoBase.setNombre("Camiseta");
        productoBase.setPrecio(20.0);
        productoBase.setPrecioFinal(20.0);
        productoBase.setDescripcion("Camiseta de algodón");
        productoBase.setMaterial("Algodón");
        productoBase.setConsideraciones("Lavar a mano");
        productoBase.setImagenUrl("http://img.com/camiseta.jpg");
        productoBase.setPromociones(List.of());

        PromotionDTO promo = new PromotionDTO();
        promo.setId(1);
        promo.setDescripcion("10% de descuento");
        promo.setDescuento(10.0);

        productoConPromo = new ProductDTO();
        productoConPromo.setId(1);
        productoConPromo.setNombre("Camiseta");
        productoConPromo.setPrecio(20.0);
        productoConPromo.setPrecioFinal(18.0);
        productoConPromo.setPromociones(List.of(promo));
    }

    //GET /productos
    @Test
    void getAllProdcuts_deberiaRetornarListaYStatus200() {
        when(productService.getAllProducts()).thenReturn(Flux.just(productoBase));

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDTO.class)
                .hasSize(1)
                .contains(productoBase);
    }

    @Test
    void getAllProducts_listVacia_deberiaRetornarArrayVacioYStatus200() {
        when(productService.getAllProducts()).thenReturn(Flux.empty());

        webTestClient.get().uri("/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDTO.class)
                .hasSize(0);
    }

    //GET /productos/{id}
    @Test
    void getProductById_existente_deberiaRetornarProductoyStatus200() {
        when(productService.getProductById(1)).thenReturn(Mono.just(productoBase));

        webTestClient.get().uri("/productos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .isEqualTo(productoBase);
    }

    @Test
    void getProductById_noExistente_deberiaRetornar404() {
        when(productService.getProductById(99)).thenReturn(Mono.empty());

        webTestClient.get().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /productos
    @Test
    void createProduct_deberiaRetornarProductoCreadoYStatus201() {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(Mono.just(productoBase));

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productoBase)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductDTO.class)
                .isEqualTo(productoBase);
    }

    @Test
    void createProduct_deberiaDelegarEnServicio() {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(Mono.just(productoBase));

        webTestClient.post().uri("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productoBase)
                .exchange()
                .expectStatus().isCreated();

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    //PUT /productos/{id}
    @Test
    void updateProduct_existente_deberiaRetornarProductoActualizadoYStatus200() {
        productoBase.setNombre("Camiseta Actualizada");
        when(productService.updateProduct(eq(1), any(ProductDTO.class))).thenReturn(Mono.just(productoBase));

        webTestClient.put().uri("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productoBase)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .isEqualTo(productoBase);
    }

    @Test
    void updateProduct_noExistente_deberiaRetornar404() {
        when(productService.updateProduct(eq(99), any(ProductDTO.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/productos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productoBase)
                .exchange()
                .expectStatus().isNotFound();
    }

    //DELETE /productos/{id}
    @Test
    void deleteProduct_existente_deberiaRetornar204() {
        when(productService.deleteProduct(1)).thenReturn(Mono.just(true));

        webTestClient.delete().uri("/productos/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteProduct_noExistente_deberiaRetornar404() {
        when(productService.deleteProduct(99)).thenReturn(Mono.just(false));

        webTestClient.delete().uri("/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //POST /productos/{productoID}/promociones/{promocionID}
    @Test
    void addPromotion_productoYPromoExistentes_deberiaRetornarProductoConPromoYStatus200() {
        when(productService.addPromotion(1, 1)).thenReturn(Mono.just(productoConPromo));

        webTestClient.post().uri("/productos/1/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .isEqualTo(productoConPromo);
    }

    @Test
    void addPromotion_productoNoExistente_deberiaRetornar404() {
        when(productService.addPromotion(99, 1)).thenReturn(Mono.empty());

        webTestClient.post().uri("/productos/99/promociones/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void addPromotion_promoNoExistente_deberiaRetornar404() {
        when(productService.addPromotion(1, 99)).thenReturn(Mono.empty());

        webTestClient.post().uri("/productos/1/promociones/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    //DELETE /productos/{productoID}/promociones/{promocionID}
    @Test
    void removePromotion_deberiaRetornarProductoSinPromoYStatus200() {
        when(productService.removePromotion(1, 1)).thenReturn(Mono.just(productoBase));

        webTestClient.delete().uri("/productos/1/promociones/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDTO.class)
                .isEqualTo(productoBase);
    }

    @Test
    void removePromotion_productoNoExistente_deberiaRetornar404() {
        when(productService.removePromotion(99, 1)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/productos/99/promociones/1")
                .exchange()
                .expectStatus().isNotFound();
    }

}
