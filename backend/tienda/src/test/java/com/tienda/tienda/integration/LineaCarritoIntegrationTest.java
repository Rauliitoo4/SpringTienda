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
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
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
        UserResponseDTO usuario = userService.createUser(userDTO);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setNombre("Camiseta Test");
        productDTO.setPrecio(20.00);
        productDTO.setDescripcion("Descripción test");
        productDTO.setMaterial("Algodón");
        productDTO.setConsideraciones("Lavar a 30 grados");
        ProductDTO producto = productService.createProduct(productDTO);

        CarritoDTO carrito = carritoService.addProductToCarrito(usuario.getCarritoId(), producto.getId(), 2);

        return carrito.getLineas().get(0);
    }

    @Test
    void obtenerTodasLasLineas_deberiaDevolver_lasLineasDeBD() {
        crearLineasTest();
        List<LineaCarritoDTO> lineas = lineaCarritoService.getAllLineas();
        
        assertNotNull(lineas);
        assertFalse(lineas.isEmpty());
    }

    @Test
    void obtenerLineaPorID_deberiaDevolver_laLineaDeBD() {
        LineaCarritoDTO linea = crearLineasTest();
        LineaCarritoDTO resultado = lineaCarritoService.getLineaById(linea.getId());

        assertNotNull(resultado);
        assertEquals(linea.getId(), resultado.getId());
    }

    @Test
    void obtenerLineaPorID_siNoExiste_deberiaDevolverNull() {
        LineaCarritoDTO resultado = lineaCarritoService.getLineaById(9999);
        assertNull(resultado);
    }

    @Test
    void actualizarCantidad_deberiaRecalcularSubtotal() {
        LineaCarritoDTO creada = crearLineasTest();

        LineaCarritoDTO actualizada = lineaCarritoService.updateLinea(creada.getId(), 5);

        assertNotNull(actualizada);
        assertEquals(5, actualizada.getCantidad());
        assertEquals(100.00, actualizada.getSubtotal());
    }

    @Test
    void eliminarLinea_deberiaEliminarlaEnBD() {
        LineaCarritoDTO creada = crearLineasTest();
        boolean eliminado = lineaCarritoService.deleteLinea(creada.getId());

        assertTrue(eliminado);
        assertNull(lineaCarritoService.getLineaById(creada.getId()));
    }

    @Test
    void eliminarLinea_deberiaRecalcularTotalDelCarrito() {
        LineaCarritoDTO creada = crearLineasTest();
        
        lineaCarritoService.deleteLinea(creada.getId());

        double total = carritoService.calcularTotal(creada.getCarritoId());
        assertEquals(0.00, total);
    }

}


