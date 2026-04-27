package com.tienda.tienda.unit.service;

import com.tienda.tienda.carrito.application.dto.CarritoDTO;
import com.tienda.tienda.carrito.application.dto.mapper.CarritoMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.CarritoEntity;
import com.tienda.tienda.carrito.application.port.CarritoRepositoryPort;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.LineaCarritoEntity;
import com.tienda.tienda.lineacarrito.application.port.LineaCarritoRepositoryPort;
import com.tienda.tienda.product.infrastructure.adapter.output.persistence.entity.ProductEntity;
import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.carrito.application.service.CarritoServiceImpl;
import com.tienda.tienda.carrito.application.helper.LineaLoader;
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
public class CarritoEntityServiceImplTest {
    
    @Mock private CarritoRepositoryPort carritoRepo;
    @Mock private ProductRepository productRepo;
    @Mock private LineaCarritoRepositoryPort lineaRepo;
    @Mock private CarritoMapper carritoMapper;
    @Mock private LineaLoader lineaLoader;

    @InjectMocks
    private CarritoServiceImpl carritoServiceImpl;

    private CarritoEntity testingCarrito() {
        CarritoEntity carritoEntity = new CarritoEntity();
        carritoEntity.setId(1);
        carritoEntity.setTotal(0.0);
        return carritoEntity;
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
        CarritoEntity carritoEntity = testingCarrito();
        when(carritoRepo.findById(1)).thenReturn(Mono.just(carritoEntity));
        when(lineaLoader.loadLineas(any(CarritoEntity.class))).thenReturn(Mono.just(carritoEntity));
        when(carritoMapper.toDTO(any(CarritoEntity.class))).thenReturn(testingDto(0.0));

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
         CarritoEntity carritoEntity = testingCarrito();
         ProductEntity productEntity = testingProduct();

         LineaCarritoEntity linea = new LineaCarritoEntity();
         linea.setId(1);
         linea.setCarritoId(1);
         linea.setProductId(1);
         linea.setSubtotal(40.0);
         linea.setQuantity(2);

         CarritoEntity updatedCarritoEntity = testingCarrito();
         updatedCarritoEntity.setTotal(40.0);

         when(carritoRepo.findById(1)).thenReturn(Mono.just(carritoEntity), Mono.just(updatedCarritoEntity));
         when(productRepo.findById(1)).thenReturn(Mono.just(productEntity));
         when(lineaRepo.save(any(LineaCarritoEntity.class))).thenReturn(Mono.just(linea));
         when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(linea));
         when(carritoRepo.save(any(CarritoEntity.class))).thenReturn(Mono.just(updatedCarritoEntity));
         when(lineaLoader.loadLineas(any(CarritoEntity.class))).thenReturn(Mono.just(updatedCarritoEntity));
         when(carritoMapper.toDTO(any(CarritoEntity.class))).thenReturn(testingDto(40.0));

         StepVerifier.create(carritoServiceImpl.addProductToCarrito(1, 1, 2))
                 .expectNextMatches(dto -> dto.getTotal() == 40.0)
                 .verifyComplete();
         verify(lineaRepo, times(1)).save(any(LineaCarritoEntity.class));
    }

    @Test
    void addProductToCarrito_ifNotExistsCarrito_shouldReturnNull() {
        when(carritoRepo.findById(999)).thenReturn(Mono.empty());
        when(productRepo.findById(1)).thenReturn(Mono.just(testingProduct()));

        StepVerifier.create(carritoServiceImpl.addProductToCarrito(999, 1, 2))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarritoEntity.class));
    }

    @Test
    void addProductToCarrito_ifNotExistsProduct_shouldReturnNull() {
        when(carritoRepo.findById(1)).thenReturn(Mono.just(testingCarrito()));
        when(productRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(carritoServiceImpl.addProductToCarrito(1, 999, 2))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarritoEntity.class));
    }

    @Test
    void calculateTotal_shouldReturn_SumOfSubtotals() {
        LineaCarritoEntity linea1 = new LineaCarritoEntity();
        linea1.setSubtotal(40.0);

        LineaCarritoEntity linea2 = new LineaCarritoEntity();
        linea2.setSubtotal(60.0);

        when(lineaRepo.findByCarritoId(1)).thenReturn(Flux.just(linea1, linea2));

        StepVerifier.create(carritoServiceImpl.calculateTotal(1))
                .expectNext(100.0)
                .verifyComplete();
    }
}
