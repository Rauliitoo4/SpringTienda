package com.tienda.tienda.unit.service;

import com.tienda.tienda.carrito.application.dto.CarritoDTO;
import com.tienda.tienda.carrito.application.dto.mapper.CarritoMapper;
import com.tienda.tienda.carrito.domain.Carrito;
import com.tienda.tienda.carrito.application.port.CarritoRepositoryPort;
import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import com.tienda.tienda.lineacarrito.application.port.LineaCarritoRepositoryPort;
import com.tienda.tienda.product.infraestructure.output.persistence.entity.ProductEntity;
import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.carrito.application.service.CarritoServiceImpl;
import com.tienda.tienda.carrito.application.service.helper.LineaLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceImplTest {
    
    @Mock private CarritoRepositoryPort carritoRepo;
    @Mock private ProductRepository productRepo;
    @Mock private LineaCarritoRepositoryPort lineaRepo;
    @Mock private CarritoMapper carritoMapper;
    @Mock private LineaLoader lineaLoader;

    @InjectMocks
    private CarritoServiceImpl carritoServiceImpl;

    private Carrito testingCarrito() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        return carrito;
    }

    private ProductEntity testingProduct() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("Camiseta");
        productEntity.setPrice(20.0);
        productEntity.setFinalPrice(20.0);
        return productEntity;
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

        StepVerifier.create(carritoServiceImpl.getCarritoById(1))
                .expectNextMatches(dto ->
                        dto.getId() == 1 &&
                        dto.getTotal() == 0.0)
                .verifyComplete();
    }

    @Test
    void getCarritoById_ifNotExists_shouldReturnNull() {
        when(carritoRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(carritoServiceImpl.getCarritoById(999))
                .verifyComplete();
    }

    @Test
    void addProductToCarrito_shouldUpdate_Total() {
         Carrito carrito = testingCarrito();
         ProductEntity productEntity = testingProduct();

         LineaCarrito linea = new LineaCarrito();
         linea.setId(1);
         linea.setCarritoId(1);
         linea.setProductId(1);
         linea.setSubtotal(40.0);
         linea.setQuantity(2);

         Carrito updatedCarrito = testingCarrito();
         updatedCarrito.setTotal(40.0);

         when(carritoRepo.findById(1)).thenReturn(Mono.just(carrito), Mono.just(updatedCarrito));
         when(productRepo.findById(1)).thenReturn(Mono.just(productEntity));
         when(lineaRepo.save(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
         when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(linea));
         when(carritoRepo.save(any(Carrito.class))).thenReturn(Mono.just(updatedCarrito));
         when(lineaLoader.loadLineas(any(Carrito.class))).thenReturn(Mono.just(updatedCarrito));
         when(carritoMapper.toDTO(any(Carrito.class))).thenReturn(testingDto(40.0));

         StepVerifier.create(carritoServiceImpl.addProductToCarrito(1, 1, 2))
                 .expectNextMatches(dto -> dto.getTotal() == 40.0)
                 .verifyComplete();
         verify(lineaRepo, times(1)).save(any(LineaCarrito.class));
    }

    @Test
    void addProductToCarrito_ifNotExistsCarrito_shouldReturnNull() {
        when(carritoRepo.findById(999)).thenReturn(Mono.empty());
        when(productRepo.findById(1)).thenReturn(Mono.just(testingProduct()));

        StepVerifier.create(carritoServiceImpl.addProductToCarrito(999, 1, 2))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void addProductToCarrito_ifNotExistsProduct_shouldReturnNull() {
        when(carritoRepo.findById(1)).thenReturn(Mono.just(testingCarrito()));
        when(productRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(carritoServiceImpl.addProductToCarrito(1, 999, 2))
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

        StepVerifier.create(carritoServiceImpl.calculateTotal(1))
                .expectNext(100.0)
                .verifyComplete();
    }
}
