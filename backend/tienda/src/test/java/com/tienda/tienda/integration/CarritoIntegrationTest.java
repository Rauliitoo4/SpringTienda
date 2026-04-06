package com.tienda.tienda.integration;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.service.CarritoService;
import com.tienda.tienda.service.ProductService;
import com.tienda.tienda.service.PromotionService;
import com.tienda.tienda.service.UserService;
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
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarritoService carritoService;

    private UserResponseDTO crearUsuarioTest() {
        UserDTO dto = new UserDTO();
        dto.setNombre("Alberto");
        dto.setApellidos("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");
        return userService.createUser(dto);
    }

    private ProductDTO crearProductoTest() {
        ProductDTO dto = new ProductDTO();
        dto.setNombre("Camiseta Test");
        dto.setPrecio(20.00);
        dto.setDescripcion("Descripción test");
        dto.setConsideraciones("Lavar a 30 grados");
        return productService.createProduct(dto);
    }

    @Test
    void obtenerCarritoPorID_deberiaDevolver_ElCarritoDeBD() {
        UserResponseDTO usuario = crearUsuarioTest();
        CarritoDTO resultado = carritoService.getCarritoById(usuario.getCarritoId());

        assertNotNull(resultado);
        assertEquals(usuario.getCarritoId(), resultado.getId());
    }

    @Test
    void obtenerCarritoPorID_siNoExiste_deberiaDevolverNull() {
        CarritoDTO resultado = carritoService.getCarritoById(9999);
        assertNull(resultado);
    }

    @Test
    void aniadirProductoAlCarrito_deberiaActualizarElTotal() {
        UserResponseDTO usuario = crearUsuarioTest();
        ProductDTO producto = crearProductoTest();

        CarritoDTO resultado = carritoService.addProductToCarrito(usuario.getCarritoId(), producto.getId(), 2);

        assertNotNull(resultado);
        assertFalse(resultado.getLineas().isEmpty());
        assertEquals(40.00, resultado.getTotal());
    }

    @Test
    void aniadirProductoAlCarrito_siCarritoNoExiste_deberiaDevolverNull() {
        ProductDTO producto = crearProductoTest();
        CarritoDTO resultado = carritoService.addProductToCarrito(9999, producto.getId(), 2);
        assertNull(resultado);
    }

    @Test
    void aniadirProductoAlCarrito_siProductoNoExiste_deberiaDevolverNull() {
        UserResponseDTO usuario = crearUsuarioTest();
        CarritoDTO resultado = carritoService.addProductToCarrito(usuario.getCarritoId(), 9999, 2);
        assertNull(resultado);
    }

    @Test
    void calcularTotal_deberiaDevolver_laSumaDeSubtotales() {
        UserResponseDTO usuario = crearUsuarioTest();
        ProductDTO producto = crearProductoTest();

        carritoService.addProductToCarrito(usuario.getCarritoId(), producto.getId(), 3);
       
        double total = carritoService.calcularTotal(usuario.getCarritoId());
        assertEquals(60.00, total);
    }
}
    



