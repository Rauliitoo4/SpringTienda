package com.tienda.tienda.integration;

import com.tienda.tienda.promotion.dto.PromotionDTO;
import com.tienda.tienda.promotion.service.PromotionService;
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
    private PromotionService promotionService;

    private PromotionDTO createPromocionTest() {
        PromotionDTO dto = new PromotionDTO();
        dto.setDescription("Descuento de verano");
        dto.setDiscount(10.0);
        return promotionService.createPromotion(dto).block();
    }

    @Test
    void createPromotion_shouldSaveInDB() {
        PromotionDTO result = createPromocionTest();

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Descuento de verano", result.getDescription());
        assertEquals(10.0, result.getDiscount());
    }

    @Test
    void getAllPromotions_shouldReturn_PromotionsFromDB() {
        createPromocionTest();
        List<PromotionDTO> promotions = promotionService.getAllPromotions().collectList().block();

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
    }

    @Test
    void getPromotionById_shouldReturn_PromotionFromDB() {
        PromotionDTO created = createPromocionTest();
        PromotionDTO result = promotionService.getPromotionById(created.getId()).block();

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getPromotionById_ifNotExists_shouldReturnNull() {
        PromotionDTO result = promotionService.getPromotionById(9999).block();
        assertNull(result);
    }

    @Test
    void updatePromotion_shouldUpdateDataInDB() {
        PromotionDTO created = createPromocionTest();

        PromotionDTO changes = new PromotionDTO();
        changes.setDiscount(20.0);

        PromotionDTO updated = promotionService.updatePromotion(created.getId(), changes).block();

        assertNotNull(updated);
        assertEquals(20.0, updated.getDiscount());
    }

    @Test
    void deletePromotion_shouldDeleteFromDB() {
        PromotionDTO created = createPromocionTest();
        boolean deleted = promotionService.deletePromotion(created.getId()).block();

        assertTrue(deleted);
        assertNull(promotionService.getPromotionById(created.getId()).block());
    }

}


