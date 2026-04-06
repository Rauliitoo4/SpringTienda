package com.tienda.tienda.integration;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.ProductService;
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
class ProductIntegrationTest {
    
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
    private PromotionService promotionService;

    private ProductDTO crearProductoTest() {
        ProductDTO dto = new ProductDTO();
        dto.setNombre("Camiseta Test");
        dto.setPrecio(20.00);
        dto.setDescripcion("Descripción test");
        dto.setConsideraciones("Lavar a 30 grados");
        return productService.createProduct(dto);
    }

    @Test
    void crearProducto_deberiaGuardarloEnBD() {
        ProductDTO resultado = crearProductoTest();

        assertNotNull(resultado.getId());
        assertEquals("Camiseta Test", resultado.getNombre());
        assertEquals(20.00, resultado.getPrecio());
        assertEquals(20.00, resultado.getPrecioFinal());
    }

    @Test
    void obtenerTodosLosProductos_deberiaDevolver_losProductosDeBD() {
        crearProductoTest();
        List<ProductDTO> productos = productService.getAllProducts();

        assertNotNull(productos);
        assertFalse(productos.isEmpty());
    }

    @Test
    void obtenerProductoPorID_deberiaDevolver_elProductoDeBD() {
        ProductDTO creado = crearProductoTest();
        ProductDTO resultado = productService.getProductById(creado.getId());

        assertNotNull(resultado);
        assertEquals(creado.getId(), resultado.getId());
    }

    @Test
    void obtenerProductoPorID_siNoExiste_deberiaDevolverNull() {
        ProductDTO resultado = productService.getProductById(9999);
        assertNull(resultado);
    }

    @Test
    void actualizarProducto_deberiaModificarLosDatosEnBD() {
        ProductDTO creado = crearProductoTest();
        
        ProductDTO cambios = new ProductDTO();
        cambios.setPrecio(29.99);

        ProductDTO actualizado = productService.updateProduct(creado.getId(), cambios);

        assertNotNull(actualizado);
        assertEquals(29.99, actualizado.getPrecio());
        assertEquals(29.99, actualizado.getPrecioFinal());
    }

    @Test
    void eliminarProducto_deberiaEliminarloEnBD() {
        ProductDTO creado = crearProductoTest();
        boolean eliminado = productService.deleteProduct(creado.getId());

        assertTrue(eliminado);
        assertNull(productService.getProductById(creado.getId()));
    }

    @Test
    void aniadirPromocion_deberiaRecalcularPrecioFinal() {
        ProductDTO producto = crearProductoTest();

        PromotionDTO promoDTO = new PromotionDTO();
        promoDTO.setDescripcion("Descuento Test");
        promoDTO.setDescuento(10.0);
        PromotionDTO promo = promotionService.createPromotion(promoDTO);

        ProductDTO resultado = productService.addPromotion(producto.getId(), promo.getId());

        assertNotNull(resultado);
        assertEquals(18.00, resultado.getPrecioFinal());
    }

    @Test
    void eliminarPromocion_deberiaRecalcularPrecioFinal() {
        ProductDTO producto = crearProductoTest();

        PromotionDTO promoDTO = new PromotionDTO();
        promoDTO.setDescripcion("Descuento Test");
        promoDTO.setDescuento(10.0);
        PromotionDTO promo = promotionService.createPromotion(promoDTO);

        productService.addPromotion(producto.getId(), promo.getId());
        ProductDTO resultado = productService.removePromotion(producto.getId(), promo.getId());

        assertNotNull(resultado);
        assertEquals(20.00, resultado.getPrecioFinal());
    }
}
