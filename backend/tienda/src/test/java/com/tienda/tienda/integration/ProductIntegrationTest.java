package com.tienda.tienda.integration;

import com.tienda.tienda.product.application.usecase.*;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.model.Size;
import com.tienda.tienda.promotion.application.usecase.CreatePromotionUseCase;
import com.tienda.tienda.promotion.domain.model.Promotion;
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
    private CreateProductUseCase createProductUseCase;

    @Autowired
    private GetProductUseCase getProductUseCase;

    @Autowired
    private UpdateProductUseCase updateProductUseCase;

    @Autowired
    private DeleteProductUseCase deleteProductUseCase;

    @Autowired
    private AddPromotionUseCase addPromotionUseCase;

    @Autowired
    private RemovePromotionUseCase removePromotionUseCase;

    @Autowired
    private CreatePromotionUseCase createPromotionUseCase;

    private Product createProductTest() {
        Product product = new Product();
        product.setName("Camiseta Test");
        product.setPrice(20.00);
        product.setFinalPrice(20.00);
        product.setDescription("Descripción test");
        product.setConsiderations("Lavar a 30 grados");
        return createProductUseCase.execute(product).block();
    }

    private Promotion createPromotionTest() {
        Promotion promotion = new Promotion();
        promotion.setDiscount(10.0);
        promotion.setDescription("Descuento Test");
        return createPromotionUseCase.execute(promotion).block();
    }

    @Test
    void createProduct_shouldSaveInDB() {
        Product result = createProductTest();

        assertNotNull(result);
        assertEquals("Camiseta Test", result.getName());
        assertEquals(20.00, result.getPrice());
        assertEquals(20.00, result.getFinalPrice());
    }

    @Test
    void createProduct_shouldHaveAllSizes() {
        Product result = createProductTest();

        assertNotNull(result.getSizes());
        assertEquals(5, result.getSizes().size());
        assertTrue(result.getSizes().containsAll(List.of(Size.values())));
    }

    @Test
    void getAllProducts_shouldReturn_theProductsFromDB() {
        createProductTest();
        var products = getProductUseCase.executeAll().collectList().block();

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void getProductById_shouldReturn_theProductFromDB() {
        Product created = createProductTest();
        Product result = getProductUseCase.execute(created.getId()).block();

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getProductById_ifNotExists_shouldReturnNull() {
        Product result = getProductUseCase.execute(9999).block();
        assertNull(result);
    }

    @Test
    void updateProduct_shouldUpdate_DataInDB() {
        Product created = createProductTest();
        
        Product changes = new Product();
        changes.setPrice(29.99);

        Product updated = updateProductUseCase.execute(created.getId(), changes).block();

        assertNotNull(updated);
        assertEquals(29.99, updated.getPrice());
        assertEquals(29.99, updated.getFinalPrice());
    }

    @Test
    void deleteProduct_shouldDeleteInDB() {
        Product created = createProductTest();
        boolean deleted = deleteProductUseCase.execute(created.getId()).block();

        assertTrue(deleted);
        assertNull(getProductUseCase.execute(created.getId()).block());
    }

    @Test
    void addPromotion_shouldRecalculateFinalPrice() {
        Product product = createProductTest();
        Promotion promotion = createPromotionTest();

        Product result = addPromotionUseCase.execute(product.getId(), promotion.getId()).block();

        assertNotNull(result);
        assertEquals(18.00, result.getFinalPrice());
    }

    @Test
    void deletePromotion_shouldRecalculateFinalPrice() {
        Product product = createProductTest();
        Promotion promotion = createPromotionTest();

        addPromotionUseCase.execute(product.getId(), promotion.getId()).block();
        Product result = removePromotionUseCase.execute(product.getId(), promotion.getId()).block();

        assertNotNull(result);
        assertEquals(20.00, result.getFinalPrice());
    }
}
