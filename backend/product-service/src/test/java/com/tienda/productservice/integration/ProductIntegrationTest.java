package com.tienda.productservice.integration;

import com.tienda.productservice.application.model.PromotionModel;
import com.tienda.productservice.application.port.output.GetPromotionOutputPort;
import com.tienda.productservice.application.usecase.*;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.domain.model.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class ProductIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("product_test")
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
        registry.add("services.promotion-service.url", () -> "http://localhost:9999");
    }

    @MockitoBean
    private GetPromotionOutputPort getPromotionOutputPort;

    @Autowired private CreateProductUseCase createProductUseCase;
    @Autowired private GetProductUseCase getProductUseCase;
    @Autowired private UpdateProductUseCase updateProductUseCase;
    @Autowired private DeleteProductUseCase deleteProductUseCase;
    @Autowired private AddPromotionUseCase addPromotionUseCase;
    @Autowired private RemovePromotionUseCase removePromotionUseCase;

    private static final PromotionModel TEST_PROMOTION = new PromotionModel(1, 10.0, "Descuento test");

    @BeforeEach
    void setUp() {
        doAnswer(invocation -> {
            List<Integer> ids = invocation.getArgument(0);
            if (ids == null || ids.isEmpty()) return Flux.empty();
            return Flux.just(TEST_PROMOTION);
        }).when(getPromotionOutputPort).findAllByIds(anyList());
    }

    private Product createProductTest() {
        Product product = new Product();
        product.setName("Camiseta Test");
        product.setPrice(20.00);
        product.setFinalPrice(20.00);
        product.setDescription("Descripción test");
        product.setConsiderations("Lavar a 30 grados");
        return createProductUseCase.execute(product).block();
    }

    @Test
    void createProduct_shouldSaveInDB() {
        Product result = createProductTest();

        assertNotNull(result);
        assertEquals("Camiseta Test", result.getName());
        assertEquals(20.00, result.getPrice());
        assertTrue(result.getFinalPrice() <= result.getPrice());
    }

    @Test
    void createProduct_shouldHaveAllSizes() {
        Product result = createProductTest();

        assertNotNull(result.getSizes());
        assertEquals(5, result.getSizes().size());
        assertTrue(result.getSizes().containsAll(List.of(Size.values())));
    }

    @Test
    void getAllProducts_shouldReturnProductsFromDB() {
        createProductTest();
        var products = getProductUseCase.executeAll().collectList().block();

        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void getProductById_shouldReturnProductFromDB() {
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
    void updateProduct_shouldUpdateDataInDB() {
        Product created = createProductTest();

        Product changes = new Product();
        changes.setPrice(29.99);

        Product updated = updateProductUseCase.execute(created.getId(), changes).block();

        assertNotNull(updated);
        assertEquals(29.99, updated.getPrice());
        assertTrue(updated.getFinalPrice() <= updated.getPrice());
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
        Product result = addPromotionUseCase.execute(product.getId(), 1).block();

        assertNotNull(result);
        assertEquals(18.00, result.getFinalPrice());
    }

    @Test
    void removePromotion_shouldRecalculateFinalPrice() {
        Product product = createProductTest();
        addPromotionUseCase.execute(product.getId(), 1).block();
        Product result = removePromotionUseCase.execute(product.getId(), 1).block();

        assertNotNull(result);
        assertEquals(result.getPrice(), result.getFinalPrice());
    }
}