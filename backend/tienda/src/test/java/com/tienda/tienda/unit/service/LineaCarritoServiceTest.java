package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.dto.mapper.LineaCarritoMapper;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.service.LineaCarritoService;
import com.tienda.tienda.service.helper.ProductLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LineaCarritoServiceTest {

    @Mock private CarritoRepository carritoRepo;
    @Mock private LineaCarritoRepository lineaRepo;
    @Mock private LineaCarritoMapper lineaCarritoMapper;
    @Mock private ProductLoader productLoader;

    @InjectMocks
    private LineaCarritoService lineaCarritoService;

    private LineaCarrito lineaDePrueba() {
        Product producto = new Product();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(20.0);
        
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setCantidad(2);
        linea.setSubtotal(40.0);
        linea.setProductoId(1);
        linea.setCarritoId(1);
        linea.setProducto(producto);
        return linea;
    }

    private LineaCarritoDTO dtoDePrueba(int cantidad, double subtotal) {
        LineaCarritoDTO dto = new LineaCarritoDTO();
        dto.setId(1);
        dto.setCantidad(cantidad);
        dto.setSubtotal(subtotal);
        dto.setCarritoId(1);
        return dto;
    }

    @Test
    void obtenerLineaPorId_deberiaDevolver_laLinea() {
        LineaCarrito linea = lineaDePrueba();
        when(lineaRepo.findById(1)).thenReturn(Mono.just(linea));
        when(productLoader.cargarProducto(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(lineaCarritoMapper.toDTO(any(LineaCarrito.class))).thenReturn(dtoDePrueba(2, 40.0));

        StepVerifier.create(lineaCarritoService.getLineaById(1))
                .expectNextMatches(dto ->
                        dto.getId() == 1 &&
                        dto.getCantidad() == 2 &&
                        dto.getSubtotal() == 40.0)
                .verifyComplete();
    }

    @Test
    void obtenerLineaPorId_siNoExiste_deberiaDevolverNull() {
        when(lineaRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(lineaCarritoService.getLineaById(999))
                .verifyComplete();
    }

    @Test
    void actualizarCantidad_deberiaRecalcular_elSubtotal() {
        LineaCarrito linea = lineaDePrueba();
        when(lineaRepo.findById(1)).thenReturn(Mono.just(linea));
        when(productLoader.cargarProducto(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(lineaRepo.save(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(linea));
        when(carritoRepo.findById(1)).thenReturn(Mono.just(new Carrito()));
        when(carritoRepo.save(any())).thenReturn(Mono.just(new Carrito()));
        when(lineaCarritoMapper.toDTO(any(LineaCarrito.class))).thenReturn(dtoDePrueba(5, 100.0));

        StepVerifier.create(lineaCarritoService.updateLinea(1, 5))
                .expectNextMatches(dto ->
                        dto.getCantidad() == 5 &&
                        dto.getSubtotal() == 100.0)
                .verifyComplete();

    }

    @Test
    void actualizarCantidad_siNoExiste_deberiaDevolverNull() {
        when(lineaRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(lineaCarritoService.updateLinea(999, 5))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void eliminarLinea_siExiste_deberiaDevolverTrue() {
        LineaCarrito linea = lineaDePrueba();
        when(lineaRepo.findById(1)).thenReturn(Mono.just(linea));
        when(lineaRepo.deleteById(1)).thenReturn(Mono.empty());
        when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.empty());
        when(carritoRepo.findById(1)).thenReturn(Mono.just(new Carrito()));
        when(carritoRepo.save(any())).thenReturn(Mono.just(new Carrito()));

        StepVerifier.create(lineaCarritoService.deleteLinea(1))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void eliminarLinea_siNoExiste_deberiaDevolverFalse() {
        when(lineaRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(lineaCarritoService.deleteLinea(999))
                .expectNext(false)
                .verifyComplete();
        verify(carritoRepo, never()).save(any());
    }
}
