package com.tienda.tienda.integration;

import com.tienda.tienda.carrito.application.usecase.*;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.product.application.usecase.CreateProductUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.application.usecase.CreateUserUseCase;
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
class CarritoIntegrationTest {
    
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
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private CreateProductUseCase createProductUseCase;

    @Autowired
    private GetCarritoUseCase getCarritoUseCase;

    @Autowired
    private AddProductToCarritoUseCase addProductToCarritoUseCase;

    @Autowired
    private GetLineaCarritoUseCase getLineaCarritoUseCase;

    @Autowired
    private UpdateLineaCarritoUseCase updateLineaCarritoUseCase;

    @Autowired
    private DeleteLineaCarritoUseCase deleteLineaCarritoUseCase;

    private User createUserTest() {
        User user = new User();
        user.setName("Alberto");
        user.setLastname("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setPassword("1234");
        return createUserUseCase.execute(user).block();
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

    private LineaCarrito createLineasTest() {
        User user = createUserTest();
        Product product = createProductTest();
        Carrito carrito = addProductToCarritoUseCase.execute(user.getCarritoId(), product.getId(), 2).block();
        return carrito.getLineas().get(0);
    }

    @Test
    void getCarritoById_shouldReturn_CarritoFromDB() {
        User user = createUserTest();
        Carrito result = getCarritoUseCase.execute(user.getCarritoId()).block();

        assertNotNull(result);
        assertEquals(user.getCarritoId(), result.getId());
    }

    @Test
    void getCarritoById_ifNotExists_shouldReturnNull() {
        Carrito result = getCarritoUseCase.execute(9999).block();
        assertNull(result);
    }

    @Test
    void addProductToCarrito_shouldUpdateTotal() {
        User user = createUserTest();
        Product product = createProductTest();

        Carrito result = addProductToCarritoUseCase.execute(user.getCarritoId(), product.getId(), 2).block();

        assertNotNull(result);
        assertFalse(result.getLineas().isEmpty());
        assertEquals(40.00, result.getTotal());
    }

    @Test
    void addProductToCarrito_ifCarritoDoesntExists_shouldReturnNull() {
        Product product = createProductTest();
        Carrito result = addProductToCarritoUseCase.execute(9999, product.getId(), 2).block();
        assertNull(result);
    }

    @Test
    void addProductToCarrito_ifProductDoesntExists_shouldReturnNull() {
        User user = createUserTest();
        Carrito result = addProductToCarritoUseCase.execute(user.getCarritoId(), 9999, 2).block();
        assertNull(result);
    }

    @Test
    void getAllLineas_shouldReturn_theLineasFromDB() {
        createLineasTest();
        List<LineaCarrito> lineas = getLineaCarritoUseCase.executeAll().collectList().block();

        assertNotNull(lineas);
        assertFalse(lineas.isEmpty());
    }

    @Test
    void getLineaById_shouldReturn_theLineaFromDB() {
        LineaCarrito linea = createLineasTest();
        LineaCarrito result = getLineaCarritoUseCase.execute(linea.getId()).block();

        assertNotNull(result);
        assertEquals(linea.getId(), result.getId());
    }

    @Test
    void getLineaById_ifNotExists_shouldReturnNull() {
        LineaCarrito resultado = getLineaCarritoUseCase.execute(9999).block();
        assertNull(resultado);
    }

    @Test
    void updateQuantity_shouldRecalculateSubtotal() {
        LineaCarrito created = createLineasTest();
        LineaCarrito updated = updateLineaCarritoUseCase.execute(created.getId(), 5).block();

        assertNotNull(updated);
        assertEquals(5, updated.getQuantity());
        assertEquals(100.00, updated.getSubtotal());
    }

    @Test
    void deleteLinea_shouldDeleteFromDB() {
        LineaCarrito created = createLineasTest();
        boolean deleted = deleteLineaCarritoUseCase.execute(created.getId()).block();

        assertTrue(deleted);
        assertNull(getLineaCarritoUseCase.execute(created.getId()).block());
    }

    @Test
    void deleteLinea_shouldRecalculateTotalFromCarrito() {
        LineaCarrito created = createLineasTest();

        deleteLineaCarritoUseCase.execute(created.getId()).block();

        Carrito carrito = getCarritoUseCase.execute(created.getCarritoId()).block();
        assertNotNull(carrito);
        assertEquals(0.00, carrito.getTotal());
    }

}
    



