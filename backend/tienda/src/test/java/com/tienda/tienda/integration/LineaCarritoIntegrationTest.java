package com.tienda.tienda.integration;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.service.CarritoService;
import com.tienda.tienda.service.ProductService;
import com.tienda.tienda.service.UserService;
import com.tienda.tienda.service.LineaCarritoService;
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

    private LineaCarritoDTO crearLineasTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setNombre("Alberto");
        userDTO.setApellidos("García");
        userDTO.setUsername("albertog");
        userDTO.setEmail("albertog@gmail.com");
        userDTO.setPassword("1234");
        UserResponseDTO usuario = userService.createUser(userDTO).block();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setNombre("Camiseta Test");
        productDTO.setPrecio(20.00);
        productDTO.setDescripcion("Descripción test");
        productDTO.setMaterial("Algodón");
        productDTO.setConsideraciones("Lavar a 30 grados");
        ProductDTO producto = productService.createProduct(productDTO).block();

        CarritoDTO carrito = carritoService.addProductToCarrito(usuario.getCarritoId(), producto.getId(), 2).block();

        return carrito.getLineas().get(0);
    }

    @Test
    void obtenerTodasLasLineas_deberiaDevolver_lasLineasDeBD() {
        crearLineasTest();
        List<LineaCarritoDTO> lineas = lineaCarritoService.getAllLineas().collectList().block();
        
        assertNotNull(lineas);
        assertFalse(lineas.isEmpty());
    }

    @Test
    void obtenerLineaPorID_deberiaDevolver_laLineaDeBD() {
        LineaCarritoDTO linea = crearLineasTest();
        LineaCarritoDTO resultado = lineaCarritoService.getLineaById(linea.getId()).block();

        assertNotNull(resultado);
        assertEquals(linea.getId(), resultado.getId());
    }

    @Test
    void obtenerLineaPorID_siNoExiste_deberiaDevolverNull() {
        LineaCarritoDTO resultado = lineaCarritoService.getLineaById(9999).block();
        assertNull(resultado);
    }

    @Test
    void actualizarCantidad_deberiaRecalcularSubtotal() {
        LineaCarritoDTO creada = crearLineasTest();

        LineaCarritoDTO actualizada = lineaCarritoService.updateLinea(creada.getId(), 5).block();

        assertNotNull(actualizada);
        assertEquals(5, actualizada.getCantidad());
        assertEquals(100.00, actualizada.getSubtotal());
    }

    @Test
    void eliminarLinea_deberiaEliminarlaEnBD() {
        LineaCarritoDTO creada = crearLineasTest();
        boolean eliminado = lineaCarritoService.deleteLinea(creada.getId()).block();

        assertTrue(eliminado);
        assertNull(lineaCarritoService.getLineaById(creada.getId()).block());
    }

    @Test
    void eliminarLinea_deberiaRecalcularTotalDelCarrito() {
        LineaCarritoDTO creada = crearLineasTest();
        
        lineaCarritoService.deleteLinea(creada.getId()).block();

        double total = carritoService.calcularTotal(creada.getCarritoId()).block();
        assertEquals(0.00, total);
    }

}


