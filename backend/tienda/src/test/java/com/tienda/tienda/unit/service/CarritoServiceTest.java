package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.dto.mapper.CarritoMapper;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.service.CarritoService;
import com.tienda.tienda.service.helper.LineaLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceTest {
    
    @Mock private CarritoRepository carritoRepo;
    @Mock private ProductRepository productRepo;
    @Mock private LineaCarritoRepository lineaRepo;
    @Mock private CarritoMapper carritoMapper;
    @Mock private LineaLoader lineaLoader;

    @InjectMocks
    private CarritoService carritoService;

    private Carrito carritoDePrueba() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        return carrito;
    }

    private Product productoDePrueba() {
        Product producto = new Product();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(20.0);
        return producto;
    }

    private CarritoDTO dtoDePrueba(double total) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(1);
        dto.setTotal(total);
        return dto;
    }

    @Test
    void obtenerCarritoPorId_deberiaDevolver_elCarrito() {
        Carrito carrito = carritoDePrueba();
        when(carritoRepo.findById(1)).thenReturn(Mono.just(carrito));
        when(lineaLoader.cargarLineas(any(Carrito.class))).thenReturn(Mono.just(carrito));
        when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(dtoDePrueba(0.0));

        StepVerifier.create(carritoService.getCarritoById(1))
                .expectNextMatches(dto ->
                        dto.getId() == 1 &&
                        dto.getTotal() == 0.0)
                .verifyComplete();
    }

    @Test
    void obtenerCarritoPorId_siNoExiste_deberiaDevolverNull() {
        when(carritoRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(carritoService.getCarritoById(999))
                .verifyComplete();
    }

    @Test
    void aniadirProductoAlCarrito_deberiaActualizar_elTotal() {
         Carrito carrito = carritoDePrueba();
         Product producto = productoDePrueba();

         LineaCarrito lineaGuardada = new LineaCarrito();
         lineaGuardada.setId(1);
         lineaGuardada.setCarritoId(1);
         lineaGuardada.setProductoId(1);
         lineaGuardada.setSubtotal(40.0);
         lineaGuardada.setCantidad(2);

         Carrito carritoActualizado = carritoDePrueba();
         carritoActualizado.setTotal(40.0);

         when(carritoRepo.findById(1)).thenReturn(Mono.just(carrito), Mono.just(carritoActualizado));
         when(productRepo.findById(1)).thenReturn(Mono.just(producto));
         when(lineaRepo.save(any(LineaCarrito.class))).thenReturn(Mono.just(lineaGuardada));
         when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(lineaGuardada));
         when(carritoRepo.save(any(Carrito.class))).thenReturn(Mono.just(carritoActualizado));
         when(lineaLoader.cargarLineas(any(Carrito.class))).thenReturn(Mono.just(carritoActualizado));
         when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(dtoDePrueba(40.0));

         StepVerifier.create(carritoService.addProductToCarrito(1, 1, 2))
                 .expectNextMatches(dto -> dto.getTotal() == 40.0)
                 .verifyComplete();
         verify(lineaRepo, times(1)).save(any(LineaCarrito.class));
    }

    @Test
    void aniadirProductoAlCarrito_siCarritoNoexiste_deberiaDevolverNull() {
        when(carritoRepo.findById(999)).thenReturn(Mono.empty());
        when(productRepo.findById(1)).thenReturn(Mono.just(productoDePrueba()));

        StepVerifier.create(carritoService.addProductToCarrito(999, 1, 2))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void aniadirProductoAlCarrito_siProductoNoexiste_deberiaDevolverNull() {
        when(carritoRepo.findById(1)).thenReturn(Mono.just(carritoDePrueba()));
        when(productRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(carritoService.addProductToCarrito(1, 999, 2))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void calcularTotal_deberiaDevolver_laSumaDeSubtotales() {
        LineaCarrito linea1 = new LineaCarrito();
        linea1.setSubtotal(40.0);

        LineaCarrito linea2 = new LineaCarrito();
        linea2.setSubtotal(60.0);

        when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(linea1, linea2));

        StepVerifier.create(carritoService.calcularTotal(1))
                .expectNext(100.0)
                .verifyComplete();
    }
}
