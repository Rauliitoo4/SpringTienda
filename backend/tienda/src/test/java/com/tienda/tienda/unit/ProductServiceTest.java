package com.tienda.tienda.unit;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.ProductRepository;
import com.tienda.tienda.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private ProductService productService;

    @Test
    void obtenerProductoPorId_deberiaDevolver_elProducto() {
        //Given
        Product producto = new Product();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20);
        producto.setPromociones(List.of());
        when(productRepo.findById(1)).thenReturn(Optional.of(producto));

        //When
        ProductDTO resultado = productService.getProductById(1);

        //Then 
        assertNotNull(resultado);
        assertEquals("Camiseta", resultado.getNombre());
        assertEquals(20, resultado.getPrecio());
    }

    @Test
    void obtenerProductoPorId_siNoExiste_deberiaDevolverNull() {
        //Given
        when(productRepo.findById(999)).thenReturn(Optional.empty());

        //When
        ProductDTO resultado = productService.getProductById(999);

        //Then
        assertNull(resultado);
    }

    @Test
    void crearProducto_deberiaGuardaryDevolverDTO() {
        //Given
        ProductDTO dto = new ProductDTO();
        dto.setNombre("Pantalón");
        dto.setPrecio(50);

        Product productoGuardado = new Product();
        productoGuardado.setId(1);
        productoGuardado.setNombre("Pantalón");
        productoGuardado.setPrecio(50);
        productoGuardado.setPromociones(List.of());
        when(productRepo.save(any(Product.class))).thenReturn(productoGuardado);

        //When
        ProductDTO resultado = productService.createProduct(dto);

        //Then
        assertNotNull(resultado);
        assertEquals("Pantalón", resultado.getNombre());
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void eliminarProducto_siExiste_deberiaDevolverTrue() {
        //Given
        when(productRepo.existsById(1)).thenReturn(true);

        //When
        boolean resultado = productService.deleteProduct(1);

        //Then
        assertTrue(resultado);
        verify(productRepo, times(1)).deleteById(1);
    }

    @Test
    void eliminarProducto_siNoExiste_deberiaDevolverFalse() {
        //Given
        when(productRepo.existsById(999)).thenReturn(false);

        //When
        boolean resultado = productService.deleteProduct(999);

        //Then
        assertFalse(resultado);
        verify(productRepo, never()).deleteById(999);
    }
}

