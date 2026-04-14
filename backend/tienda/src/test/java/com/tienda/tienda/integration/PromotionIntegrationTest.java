package com.tienda.tienda.integration;

import com.tienda.tienda.dto.PromotionDTO;
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

    private PromotionDTO crearPromocionTest() {
        PromotionDTO dto = new PromotionDTO();
        dto.setDescripcion("Descuento de verano");
        dto.setDescuento(10.0);
        return promotionService.createPromotion(dto).block();
    }

    @Test
    void crearPromocion_deberiaGuardarlaEnBD() {
        PromotionDTO resultado = crearPromocionTest();

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Descuento de verano", resultado.getDescripcion());
        assertEquals(10.0, resultado.getDescuento());
    }

    @Test
    void obtenerTodasLasPromociones_deberiaDevolver_lasPromocionesDeBD() {
        crearPromocionTest();
        List<PromotionDTO> promociones = promotionService.getAllPromotions().collectList().block();

        assertNotNull(promociones);
        assertFalse(promociones.isEmpty());
    }

    @Test
    void obtenerPromocionPorID_deberiaDevolver_laPromocionDeBD() {
        PromotionDTO creada = crearPromocionTest();
        PromotionDTO resultado = promotionService.getPromotionById(creada.getId()).block();

        assertNotNull(resultado);
        assertEquals(creada.getId(), resultado.getId());
    }

    @Test
    void obtenerPromocionPorID_siNoExiste_deberiaDevolverNull() {
        PromotionDTO resultado = promotionService.getPromotionById(9999).block();
        assertNull(resultado);
    }

    @Test
    void actualizarPromocion_deberiaModificarLosDatosEnBD() {
        PromotionDTO creada = crearPromocionTest();

        PromotionDTO cambios = new PromotionDTO();
        cambios.setDescuento(20.0);

        PromotionDTO actualizada = promotionService.updatePromotion(creada.getId(), cambios).block();

        assertNotNull(actualizada);
        assertEquals(20.0, actualizada.getDescuento());
    }

    @Test
    void eliminarPromocion_deberiaEliminarlaEnBD() {
        PromotionDTO creada = crearPromocionTest();
        boolean eliminado = promotionService.deletePromotion(creada.getId()).block();

        assertTrue(eliminado);
        assertNull(promotionService.getPromotionById(creada.getId()).block());
    }

}


