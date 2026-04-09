package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.ProductRepository;
import com.tienda.tienda.repository.CarritoRepository;
import com.tienda.tienda.repository.LineaCarritoRepository;
import com.tienda.tienda.service.CarritoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {
    
    @Mock
    private CarritoRepository carritoRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private LineaCarritoRepository lineaRepo;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    void obtenerCarritoPorId_deberiaDevolver_elCarrito() {
        //Given
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        carrito.setLineas(new ArrayList<>());
        when(carritoRepo.findById(1)).thenReturn(Optional.of(carrito));

        //When
        CarritoDTO resultado = carritoService.getCarritoById(1);

        //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(0.0, resultado.getTotal());
    }

    @Test
    void obtenerCarritoPorId_siNoExiste_deberiaDevolverNull() {
        //Given
        when(carritoRepo.findById(999)).thenReturn(Optional.empty());

        //When
        CarritoDTO resultado = carritoService.getCarritoById(999);

        //Then
        assertNull(resultado);
    }

    @Test
    void aniadirProductoAlCarrito_deberiaActualizar_elTotal() {
        //Given
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        carrito.setLineas(new ArrayList<>());

        Product producto = new Product();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(20.0);
        
        when(carritoRepo.findById(1)).thenReturn(Optional.of(carrito));
        when(productRepo.findById(1)).thenReturn(Optional.of(producto));
        when(lineaRepo.save(any(LineaCarrito.class))).thenReturn(new LineaCarrito());
        when(carritoRepo.save(any(Carrito.class))).thenReturn(carrito);

        //When
        CarritoDTO resultado = carritoService.addProductToCarrito(1, 1, 2);

        //Then
        assertNotNull(resultado);
        assertEquals(40.0, resultado.getTotal());
        verify(lineaRepo, times(1)).save(any(LineaCarrito.class));
        verify(carritoRepo, times(1)).save(any(Carrito.class));
    }

    @Test
    void aniadirProductoAlCarrito_siCarritoNoexiste_deberiaDevolverNull() {
        //Given
        when(carritoRepo.findById(999)).thenReturn(Optional.empty());

        //When
        CarritoDTO resultado = carritoService.addProductToCarrito(999, 1, 2);

        //Then
        assertNull(resultado);
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void aniadirProductoAlCarrito_siProductoNoexiste_deberiaDevolverNull() {
        //Given
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        carrito.setLineas(new ArrayList<>());
        when(carritoRepo.findById(1)).thenReturn(Optional.of(carrito));
        when(productRepo.findById(999)).thenReturn(Optional.empty());

        //When
        CarritoDTO resultado = carritoService.addProductToCarrito(1, 999, 2);

        //Then
        assertNull(resultado);
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void calcularTotal_deberiaDevolver_laSumaDeSubtotales() {
        //Given
        LineaCarrito linea1 = new LineaCarrito();
        linea1.setSubtotal(40.0);

        LineaCarrito linea2 = new LineaCarrito();
        linea2.setSubtotal(60.0);

        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setLineas(new ArrayList<>());
        carrito.getLineas().add(linea1);
        carrito.getLineas().add(linea2);
        when(carritoRepo.findById(1)).thenReturn(Optional.of(carrito));

        //When
        double total = carritoService.calcularTotal(1);

        //Then
        assertEquals(100.0, total);
    }
}
