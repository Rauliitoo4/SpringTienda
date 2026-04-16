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

    private Carrito testingCarrito() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        return carrito;
    }

    private Product testingProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        return product;
    }

    private CarritoDTO testingDto(double total) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(1);
        dto.setTotal(total);
        return dto;
    }

    @Test
    void getCarritoById_shouldReturn_Carrito() {
        Carrito carrito = testingCarrito();
        when(carritoRepo.findById(1)).thenReturn(Mono.just(carrito));
        when(lineaLoader.loadLineas(any(Carrito.class))).thenReturn(Mono.just(carrito));
        when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(testingDto(0.0));

        StepVerifier.create(carritoService.getCarritoById(1))
                .expectNextMatches(dto ->
                        dto.getId() == 1 &&
                        dto.getTotal() == 0.0)
                .verifyComplete();
    }

    @Test
    void getCarritoById_ifNotExists_shouldReturnNull() {
        when(carritoRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(carritoService.getCarritoById(999))
                .verifyComplete();
    }

    @Test
    void addProductToCarrito_shouldUpdate_Total() {
         Carrito carrito = testingCarrito();
         Product product = testingProduct();

         LineaCarrito linea = new LineaCarrito();
         linea.setId(1);
         linea.setCarritoId(1);
         linea.setProductId(1);
         linea.setSubtotal(40.0);
         linea.setQuantity(2);

         Carrito updatedCarrito = testingCarrito();
         updatedCarrito.setTotal(40.0);

         when(carritoRepo.findById(1)).thenReturn(Mono.just(carrito), Mono.just(updatedCarrito));
         when(productRepo.findById(1)).thenReturn(Mono.just(product));
         when(lineaRepo.save(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
         when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(linea));
         when(carritoRepo.save(any(Carrito.class))).thenReturn(Mono.just(updatedCarrito));
         when(lineaLoader.loadLineas(any(Carrito.class))).thenReturn(Mono.just(updatedCarrito));
         when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(testingDto(40.0));

         StepVerifier.create(carritoService.addProductToCarrito(1, 1, 2))
                 .expectNextMatches(dto -> dto.getTotal() == 40.0)
                 .verifyComplete();
         verify(lineaRepo, times(1)).save(any(LineaCarrito.class));
    }

    @Test
    void addProductToCarrito_ifNotExistsCarrito_shouldReturnNull() {
        when(carritoRepo.findById(999)).thenReturn(Mono.empty());
        when(productRepo.findById(1)).thenReturn(Mono.just(testingProduct()));

        StepVerifier.create(carritoService.addProductToCarrito(999, 1, 2))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void addProductToCarrito_ifNotExistsProduct_shouldReturnNull() {
        when(carritoRepo.findById(1)).thenReturn(Mono.just(testingCarrito()));
        when(productRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(carritoService.addProductToCarrito(1, 999, 2))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void calculateTotal_shouldReturn_SumOfSubtotals() {
        LineaCarrito linea1 = new LineaCarrito();
        linea1.setSubtotal(40.0);

        LineaCarrito linea2 = new LineaCarrito();
        linea2.setSubtotal(60.0);

        when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(linea1, linea2));

        StepVerifier.create(carritoService.calculateTotal(1))
                .expectNext(100.0)
                .verifyComplete();
    }
}
