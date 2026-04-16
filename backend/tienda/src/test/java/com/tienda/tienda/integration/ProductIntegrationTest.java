package com.tienda.tienda.integration;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.ProductService;
import com.tienda.tienda.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ProductIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("tienda_test")
            .withUsername("postgres")
            .withPassword("admin123");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgres.getHost() + ":" + postgres.getMappedPort(5432)
                + "/" + postgres.getDatabaseName());
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);

        registry.add("spring.liquibase.url", postgres::getJdbcUrl);
        registry.add("spring.liquibase.user", postgres::getUsername);
        registry.add("spring.liquibase.password", postgres::getPassword);
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;

    private ProductDTO createProductTest() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Camiseta Test");
        dto.setPrice(20.00);
        dto.setDescription("Descripción test");
        dto.setConsiderations("Lavar a 30 grados");
        return productService.createProduct(dto).block();
    }

    @Test
    void createProduct_shouldSaveInDB() {
        ProductDTO result = createProductTest();

        assertNotNull(result);
        assertEquals("Camiseta Test", result.getName());
        assertEquals(20.00, result.getPrice());
        assertEquals(20.00, result.getFinalPrice());
    }

    @Test
    void getAllProducts_shouldReturn_theProductsFromDB() {
        createProductTest();
        var products = productService.getAllProducts().collectList().block();

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void getProductById_shouldReturn_theProductFromDB() {
        ProductDTO created = createProductTest();
        ProductDTO result = productService.getProductById(created.getId()).block();

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getProductById_ifNotExists_shouldReturnNull() {
        ProductDTO result = productService.getProductById(9999).block();
        assertNull(result);
    }

    @Test
    void updateProduct_shouldUpdate_DataInDB() {
        ProductDTO created = createProductTest();
        
        ProductDTO changes = new ProductDTO();
        changes.setPrice(29.99);

        ProductDTO updated = productService.updateProduct(created.getId(), changes).block();

        assertNotNull(updated);
        assertEquals(29.99, updated.getPrice());
        assertEquals(29.99, updated.getFinalPrice());
    }

    @Test
    void deleteProduct_shouldDeleteInDB() {
        ProductDTO created = createProductTest();
        boolean deleted = productService.deleteProduct(created.getId()).block();

        assertTrue(deleted);
        assertNull(productService.getProductById(created.getId()).block());
    }

    @Test
    void addPromotion_shouldRecalculateFinalPrice() {
        ProductDTO product = createProductTest();

        PromotionDTO promoDTO = new PromotionDTO();
        promoDTO.setDescription("Descuento Test");
        promoDTO.setDiscount(10.0);
        PromotionDTO promo = promotionService.createPromotion(promoDTO).block();

        ProductDTO result = productService.addPromotion(product.getId(), promo.getId()).block();

        assertNotNull(result);
        assertEquals(18.00, result.getFinalPrice());
    }

    @Test
    void deletePromotion_shouldRecalculateFinalPrice() {
        ProductDTO product = createProductTest();

        PromotionDTO promoDTO = new PromotionDTO();
        promoDTO.setDescription("Descuento Test");
        promoDTO.setDiscount(10.0);
        PromotionDTO promo = promotionService.createPromotion(promoDTO).block();

        productService.addPromotion(product.getId(), promo.getId()).block();
        ProductDTO result = productService.removePromotion(product.getId(), promo.getId()).block();

        assertNotNull(result);
        assertEquals(20.00, result.getFinalPrice());
    }
}
