package com.tienda.tienda.integration;

import com.tienda.tienda.carrito.application.dto.CarritoDTO;
import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request.UserRequest;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import com.tienda.tienda.carrito.application.service.CarritoService;
import com.tienda.tienda.product.application.service.ProductService;
import com.tienda.tienda.user.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CarritoEntityIntegrationTest {
    
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
    private UserService userService;

    @Autowired
    private CarritoService carritoService;

    private UserResponse createUserTest() {
        UserRequest dto = new UserRequest();
        dto.setName("Alberto");
        dto.setLastname("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");
        return userService.createUser(dto).block();
    }

    private ProductResponse createProductTest() {
        ProductResponse dto = new ProductResponse();
        dto.setName("Camiseta Test");
        dto.setPrice(20.00);
        dto.setDescription("Descripción test");
        dto.setConsiderations("Lavar a 30 grados");
        return productService.createProduct(dto).block();
    }

    @Test
    void getCarritoById_shouldReturn_CarritoFromDB() {
        UserResponse user = createUserTest();
        CarritoDTO result = carritoService.getCarritoById(user.getCarritoId()).block();

        assertNotNull(result);
        assertEquals(user.getCarritoId(), result.getId());
    }

    @Test
    void getCarritoById_ifNotExists_shouldReturnNull() {
        CarritoDTO result = carritoService.getCarritoById(9999).block();
        assertNull(result);
    }

    @Test
    void addProductToCarrito_shouldUpdateTotal() {
        UserResponse user = createUserTest();
        ProductResponse product = createProductTest();

        CarritoDTO result = carritoService.addProductToCarrito(user.getCarritoId(), product.getId(), 2).block();

        assertNotNull(result);
        assertFalse(result.getLineas().isEmpty());
        assertEquals(40.00, result.getTotal());
    }

    @Test
    void addProductToCarrito_ifCarritoDoesntExists_shouldReturnNull() {
        ProductResponse product = createProductTest();
        CarritoDTO result = carritoService.addProductToCarrito(9999, product.getId(), 2).block();
        assertNull(result);
    }

    @Test
    void addProductToCarrito_ifProductDoesntExists_shouldReturnNull() {
        UserResponse user = createUserTest();
        CarritoDTO result = carritoService.addProductToCarrito(user.getCarritoId(), 9999, 2).block();
        assertNull(result);
    }

    @Test
    void calculateTotal_shouldReturn_theSumOfSubtotals() {
        UserResponse user = createUserTest();
        ProductResponse product = createProductTest();

        carritoService.addProductToCarrito(user.getCarritoId(), product.getId(), 3).block();
       
        double total = carritoService.calculateTotal(user.getCarritoId()).block();
        assertEquals(60.00, total);
    }
}
    



