package com.tienda.tienda.integration;

import com.tienda.tienda.carrito.application.dto.CarritoDTO;
import com.tienda.tienda.product.application.dto.ProductDTO;
import com.tienda.tienda.user.application.dto.UserDTO;
import com.tienda.tienda.user.application.dto.UserResponseDTO;
import com.tienda.tienda.lineacarrito.application.dto.LineaCarritoDTO;
import com.tienda.tienda.carrito.application.service.CarritoService;
import com.tienda.tienda.product.application.service.ProductService;
import com.tienda.tienda.user.application.service.UserService;
import com.tienda.tienda.lineacarrito.application.service.LineaCarritoService;
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
class LineaCarritoIntegrationTest {
    
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
    private CarritoService carritoService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private LineaCarritoService lineaCarritoService;

    private LineaCarritoDTO createLineasTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Alberto");
        userDTO.setLastname("García");
        userDTO.setUsername("albertog");
        userDTO.setEmail("albertog@gmail.com");
        userDTO.setPassword("1234");
        UserResponseDTO user = userService.createUser(userDTO).block();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Camiseta Test");
        productDTO.setPrice(20.00);
        productDTO.setDescription("Descripción test");
        productDTO.setMaterial("Algodón");
        productDTO.setConsiderations("Lavar a 30 grados");
        ProductDTO product = productService.createProduct(productDTO).block();

        CarritoDTO carrito = carritoService.addProductToCarrito(user.getCarritoId(), product.getId(), 2).block();

        return carrito.getLineas().get(0);
    }

    @Test
    void getAllLineas_shouldReturn_theLineasFromDB() {
        createLineasTest();
        List<LineaCarritoDTO> lineas = lineaCarritoService.getAllLineas().collectList().block();
        
        assertNotNull(lineas);
        assertFalse(lineas.isEmpty());
    }

    @Test
    void getLineaById_shouldReturn_theLineaFromDB() {
        LineaCarritoDTO linea = createLineasTest();
        LineaCarritoDTO result = lineaCarritoService.getLineaById(linea.getId()).block();

        assertNotNull(result);
        assertEquals(linea.getId(), result.getId());
    }

    @Test
    void getLineaById_ifNotExists_shouldReturnNull() {
        LineaCarritoDTO resultado = lineaCarritoService.getLineaById(9999).block();
        assertNull(resultado);
    }

    @Test
    void updateQuantity_shouldRecalculateSubtotal() {
        LineaCarritoDTO created = createLineasTest();

        LineaCarritoDTO updated = lineaCarritoService.updateLinea(created.getId(), 5).block();

        assertNotNull(updated);
        assertEquals(5, updated.getQuantity());
        assertEquals(100.00, updated.getSubtotal());
    }

    @Test
    void deleteLinea_shouldDeleteFromDB() {
        LineaCarritoDTO created = createLineasTest();
        boolean deleted = lineaCarritoService.deleteLinea(created.getId()).block();

        assertTrue(deleted);
        assertNull(lineaCarritoService.getLineaById(created.getId()).block());
    }

    @Test
    void deleteLinea_shouldRecalculateTotalFromCarrito() {
        LineaCarritoDTO created = createLineasTest();
        
        lineaCarritoService.deleteLinea(created.getId()).block();

        double total = carritoService.calculateTotal(created.getCarritoId()).block();
        assertEquals(0.00, total);
    }

}


