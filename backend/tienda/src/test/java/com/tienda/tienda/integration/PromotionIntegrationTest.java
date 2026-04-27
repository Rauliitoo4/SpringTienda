package com.tienda.tienda.integration;

import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.application.usecase.*;
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
class PromotionIntegrationTest {
    
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
    private CreatePromotionUseCase createPromotionUseCase;

    @Autowired
    private GetPromotionUseCase getPromotionUseCase;

    @Autowired
    private UpdatePromotionUseCase updatePromotionUseCase;

    @Autowired
    private DeletePromotionUseCase deletePromotionUseCase;

    private Promotion createPromocionTest() {
        Promotion promotion = new Promotion();
        promotion.setDescription("Descuento de verano");
        promotion.setDiscount(10.0);
        return createPromotionUseCase.execute(promotion).block();
    }

    @Test
    void createPromotion_shouldSaveInDB() {
        Promotion result = createPromocionTest();

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Descuento de verano", result.getDescription());
        assertEquals(10.0, result.getDiscount());
    }

    @Test
    void getAllPromotions_shouldReturn_PromotionsFromDB() {
        createPromocionTest();
        List<Promotion> promotions = getPromotionUseCase.executeAll().collectList().block();

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
    }

    @Test
    void getPromotionById_shouldReturn_PromotionFromDB() {
        Promotion created = createPromocionTest();
        Promotion result = getPromotionUseCase.execute(created.getId()).block();

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getPromotionById_ifNotExists_shouldReturnNull() {
        Promotion result = getPromotionUseCase.execute(9999).block();
        assertNull(result);
    }

    @Test
    void updatePromotion_shouldUpdateDataInDB() {
        Promotion created = createPromocionTest();

        Promotion changes = new Promotion();
        changes.setDiscount(20.0);

        Promotion updated = updatePromotionUseCase.execute(created.getId(), changes).block();

        assertNotNull(updated);
        assertEquals(20.0, updated.getDiscount());
    }

    @Test
    void deletePromotion_shouldDeleteFromDB() {
        Promotion created = createPromocionTest();
        boolean deleted = deletePromotionUseCase.execute(created.getId()).block();

        assertTrue(deleted);
        assertNull(getPromotionUseCase.execute(created.getId()).block());
    }

}


