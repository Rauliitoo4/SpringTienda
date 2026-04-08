package com.tienda.tienda.unit;

import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.CarritoRepository;
import com.tienda.tienda.repository.LineaCarritoRepository;
import com.tienda.tienda.service.LineaCarritoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LineaCarritoServiceTest {
    
    @Mock
    private CarritoRepository carritoRepo;

    @Mock
    private LineaCarritoRepository lineaRepo;

    @InjectMocks
    private LineaCarritoService lineaCarritoService;

    private LineaCarrito crearLineaTest() {
        Product producto = new Product();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(20.0);
        producto.setPromociones(new ArrayList<>());

        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setCantidad(2);
        linea.setSubtotal(40.0);
        linea.setProducto(producto);
        linea.setCarrito(carrito);

        carrito.setLineas(new ArrayList<>(List.of(linea)));

        return linea;
    }

    @Test
    void obtenerLineaPorId_deberiaDevolver_laLinea() {
        //Given
        LineaCarrito linea = crearLineaTest();
        when(lineaRepo.findById(1)).thenReturn(Optional.of(linea));

        //When
        LineaCarritoDTO resultado = lineaCarritoService.getLineaById(1);

        //Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(2, resultado.getCantidad());
        assertEquals(40.0, resultado.getSubtotal());
    }

    @Test
    void obtenerLineaPorId_siNoExiste_deberiaDevolverNull() {
        //Given
        when(lineaRepo.findById(999)).thenReturn(Optional.empty());

        //When
        LineaCarritoDTO resultado = lineaCarritoService.getLineaById(999);

        //Then
        assertNull(resultado);
    }

    @Test
    void actualizarCantidad_deberiaRecalcular_elSubtotal() {
        //Given
        LineaCarrito linea = crearLineaTest();
        when(lineaRepo.findById(1)).thenReturn(Optional.of(linea));
        when(lineaRepo.save(any(LineaCarrito.class))).thenReturn(linea);

        //When
        LineaCarritoDTO resultado = lineaCarritoService.updateLinea(1, 5);

        //Then
        assertNotNull(resultado);
        assertEquals(5, resultado.getCantidad());
        assertEquals(100.0, resultado.getSubtotal());
    }

    @Test
    void actualizarCantidad_siNoExiste_deberiaDevolverNull() {
        //Given
        when(lineaRepo.findById(999)).thenReturn(Optional.empty());

        //When
        LineaCarritoDTO resultado = lineaCarritoService.updateLinea(999, 5);

        //Then
        assertNull(resultado);
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void eliminarLinea_siExiste_deberiaDevolverTrue() {
        //Given
        LineaCarrito linea = crearLineaTest();
        when(lineaRepo.findById(1)).thenReturn(Optional.of(linea));

        //When
        boolean resultado = lineaCarritoService.deleteLinea(1);

        //Then
        assertTrue(resultado);
        verify(carritoRepo, times(1)).save(any(Carrito.class));
    }

    @Test
    void eliminarLinea_siNoExiste_deberiaDevolverFalse() {
        //Given
        when(lineaRepo.findById(999)).thenReturn(Optional.empty());

        //When
        boolean resultado = lineaCarritoService.deleteLinea(999);

        //Then
        assertFalse(resultado);
        verify(carritoRepo, never()).save(any(Carrito.class));
    }
}
