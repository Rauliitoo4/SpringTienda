/*package com.tienda.carritoservice.integration;

import com.tienda.carritoservice.application.service.ProductLoader;
import com.tienda.carritoservice.application.usecase.*;
import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Product;
import com.tienda.carritoservice.domain.model.Size;
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
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class CarritoIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("carrito_test")
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
        registry.add("services.product-service.url", () -> "http://localhost:9999");
    }

    @MockitoBean
    private ProductLoader productLoader;

    @Autowired private CreateCarritoUseCase createCarritoUseCase;
    @Autowired private GetCarritoUseCase getCarritoUseCase;
    @Autowired private AddProductToCarritoUseCase addProductToCarritoUseCase;
    @Autowired private GetLineaCarritoUseCase getLineaCarritoUseCase;
    @Autowired private UpdateLineaCarritoUseCase updateLineaCarritoUseCase;
    @Autowired private DeleteLineaCarritoUseCase deleteLineaCarritoUseCase;

    private static final Product TEST_PRODUCT = new Product(1, "Camiseta Test", 20.0, 20.0, null, null);

    @BeforeEach
    void setUp() {
        when(productLoader.loadProduct(1)).thenReturn(Mono.just(TEST_PRODUCT));
        when(productLoader.loadProduct(9999)).thenReturn(Mono.empty());
    }

    private Carrito createCarritoTest() {
        return createCarritoUseCase.execute().block();
    }

    private LineaCarrito createLineaTest() {
        Carrito carrito = createCarritoTest();
        Carrito updated = addProductToCarritoUseCase.execute(carrito.getId(), 1, 2, Size.M).block();
        return updated.getLineas().get(0);
    }

    @Test
    void createCarrito_shouldReturnCarritoWithTotalZero() {
        Carrito result = createCarritoTest();

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(0.0, result.getTotal());
    }

    @Test
    void getCarritoById_shouldReturnCarritoFromDB() {
        Carrito carrito = createCarritoTest();
        Carrito result = getCarritoUseCase.execute(carrito.getId()).block();

        assertNotNull(result);
        assertEquals(carrito.getId(), result.getId());
    }

    @Test
    void getCarritoById_ifNotExists_shouldReturnNull() {
        Carrito result = getCarritoUseCase.execute(9999).block();
        assertNull(result);
    }

    @Test
    void addProductToCarrito_shouldUpdateTotal() {
        Carrito carrito = createCarritoTest();
        Carrito result = addProductToCarritoUseCase.execute(carrito.getId(), 1, 2, Size.M).block();

        assertNotNull(result);
        assertFalse(result.getLineas().isEmpty());
        assertEquals(40.0, result.getTotal());
    }

    @Test
    void addProductToCarrito_ifCarritoNotExists_shouldReturnNull() {
        Carrito result = addProductToCarritoUseCase.execute(9999, 1, 2, Size.M).block();
        assertNull(result);
    }

    @Test
    void addProductToCarrito_ifProductNotExists_shouldReturnNull() {
        Carrito carrito = createCarritoTest();
        Carrito result = addProductToCarritoUseCase.execute(carrito.getId(), 9999, 2, Size.M).block();
        assertNull(result);
    }

    @Test
    void addProductToCarrito_sameSizeSameProduct_shouldAccumulateQuantity() {
        Carrito carrito = createCarritoTest();

        addProductToCarritoUseCase.execute(carrito.getId(), 1, 2, Size.M).block();
        Carrito result = addProductToCarritoUseCase.execute(carrito.getId(), 1, 3, Size.M).block();

        assertNotNull(result);
        assertEquals(1, result.getLineas().size());
        assertEquals(5, result.getLineas().get(0).getQuantity());
        assertEquals(100.0, result.getTotal());
    }

    @Test
    void addProductToCarrito_differentSize_shouldCreateNewLinea() {
        Carrito carrito = createCarritoTest();

        addProductToCarritoUseCase.execute(carrito.getId(), 1, 2, Size.M).block();
        Carrito result = addProductToCarritoUseCase.execute(carrito.getId(), 1, 2, Size.L).block();

        assertNotNull(result);
        assertEquals(2, result.getLineas().size());
        assertEquals(80.0, result.getTotal());
    }

    @Test
    void getAllLineas_shouldReturnLineasFromDB() {
        createLineaTest();
        List<LineaCarrito> lineas = getLineaCarritoUseCase.executeAll().collectList().block();

        assertNotNull(lineas);
        assertFalse(lineas.isEmpty());
    }

    @Test
    void getLineaById_shouldReturnLineaFromDB() {
        LineaCarrito linea = createLineaTest();
        LineaCarrito result = getLineaCarritoUseCase.execute(linea.getId()).block();

        assertNotNull(result);
        assertEquals(linea.getId(), result.getId());
    }

    @Test
    void getLineaById_ifNotExists_shouldReturnNull() {
        LineaCarrito result = getLineaCarritoUseCase.execute(9999).block();
        assertNull(result);
    }

    @Test
    void updateQuantity_shouldRecalculateSubtotal() {
        LineaCarrito linea = createLineaTest();
        LineaCarrito updated = updateLineaCarritoUseCase.execute(linea.getId(), 5).block();

        assertNotNull(updated);
        assertEquals(5, updated.getQuantity());
        assertEquals(100.0, updated.getSubtotal());
    }

    @Test
    void deleteLinea_shouldReturnTrue() {
        LineaCarrito linea = createLineaTest();
        boolean deleted = deleteLineaCarritoUseCase.execute(linea.getId()).block();

        assertTrue(deleted);
        assertNull(getLineaCarritoUseCase.execute(linea.getId()).block());
    }

    @Test
    void deleteLinea_shouldRecalculateTotalFromCarrito() {
        LineaCarrito linea = createLineaTest();
        deleteLineaCarritoUseCase.execute(linea.getId()).block();

        Carrito carrito = getCarritoUseCase.execute(linea.getCarritoId()).block();
        assertNotNull(carrito);
        assertEquals(0.0, carrito.getTotal());
    }
}*/