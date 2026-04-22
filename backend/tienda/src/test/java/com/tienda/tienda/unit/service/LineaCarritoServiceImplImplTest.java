package com.tienda.tienda.unit.service;

import com.tienda.tienda.carrito.repository.port.CarritoRepositoryPort;
import com.tienda.tienda.lineacarrito.dto.LineaCarritoDTO;
import com.tienda.tienda.lineacarrito.dto.mapper.LineaCarritoMapper;
import com.tienda.tienda.lineacarrito.model.LineaCarrito;
import com.tienda.tienda.lineacarrito.repository.port.LineaCarritoRepositoryPort;
import com.tienda.tienda.product.model.Product;
import com.tienda.tienda.carrito.service.CarritoService;
import com.tienda.tienda.lineacarrito.service.LineaCarritoServiceImpl;
import com.tienda.tienda.lineacarrito.service.helper.ProductLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LineaCarritoServiceImplImplTest {

    @Mock private CarritoRepositoryPort carritoRepo;
    @Mock private LineaCarritoRepositoryPort lineaRepo;
    @Mock private LineaCarritoMapper lineaCarritoMapper;
    @Mock private ProductLoader productLoader;
    @Mock private CarritoService carritoService;

    @InjectMocks
    private LineaCarritoServiceImpl lineaCarritoServiceImpl;

    private LineaCarrito testingLinea() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);

        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(40.0);
        linea.setProductId(1);
        linea.setCarritoId(1);
        linea.setProduct(product);
        return linea;
    }

    private LineaCarritoDTO testingDto(int quantity, double subtotal) {
        LineaCarritoDTO dto = new LineaCarritoDTO();
        dto.setId(1);
        dto.setQuantity(quantity);
        dto.setSubtotal(subtotal);
        dto.setCarritoId(1);
        return dto;
    }

    @Test
    void getLineaById_shouldReturn_Linea() {
        LineaCarrito linea = testingLinea();
        when(lineaRepo.findById(1)).thenReturn(Mono.just(linea));
        when(productLoader.loadProduct(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(lineaCarritoMapper.toDTO(any(LineaCarrito.class))).thenReturn(testingDto(2, 40.0));

        StepVerifier.create(lineaCarritoServiceImpl.getLineaById(1))
                .expectNextMatches(dto ->
                        dto.getId() == 1 &&
                        dto.getQuantity() == 2 &&
                        dto.getSubtotal() == 40.0)
                .verifyComplete();
    }

    @Test
    void getLineaById_ifNotExists_shouldReturnNull() {
        when(lineaRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(lineaCarritoServiceImpl.getLineaById(999))
                .verifyComplete();
    }

    @Test
    void updateQuantity_shouldRecalculate_subtotal() {
        LineaCarrito linea = testingLinea();
        when(lineaRepo.findById(1)).thenReturn(Mono.just(linea));
        when(productLoader.loadProduct(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(lineaRepo.save(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(carritoService.recalculateTotal(anyInt())).thenReturn(Mono.empty());
        when(lineaCarritoMapper.toDTO(any(LineaCarrito.class))).thenReturn(testingDto(5, 100.0));

        StepVerifier.create(lineaCarritoServiceImpl.updateLinea(1, 5))
                .expectNextMatches(dto ->
                        dto.getQuantity() == 5 &&
                        dto.getSubtotal() == 100.0)
                .verifyComplete();

    }

    @Test
    void updateQuantity_ifNotExists_shouldReturnNull() {
        when(lineaRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(lineaCarritoServiceImpl.updateLinea(999, 5))
                .verifyComplete();
        verify(lineaRepo, never()).save(any(LineaCarrito.class));
    }

    @Test
    void deleteLinea_shouldReturnTrue() {
        LineaCarrito linea = testingLinea();
        when(lineaRepo.findById(1)).thenReturn(Mono.just(linea));
        when(lineaRepo.deleteById(1)).thenReturn(Mono.empty());
        when(carritoService.recalculateTotal(anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(lineaCarritoServiceImpl.deleteLinea(1))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void deleteLinea_ifNotExists_shouldReturnFalse() {
        when(lineaRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(lineaCarritoServiceImpl.deleteLinea(999))
                .expectNext(false)
                .verifyComplete();
        verify(carritoRepo, never()).save(any());
    }
}
