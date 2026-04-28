package com.tienda.tienda.unit.usecase.carrito;

import com.tienda.tienda.carrito.application.helper.LineaLoader;
import com.tienda.tienda.carrito.application.helper.TotalCalculator;
import com.tienda.tienda.carrito.application.usecase.AddProductToCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.CreateLineaCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.GetCarritoRepository;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductToCarritoUseCaseTest {

    @Mock private GetCarritoRepository getCarritoRepository;
    @Mock private CreateLineaCarritoRepository createLineaCarritoRepository;
    @Mock private GetProductRepository getProductRepository;
    @Mock private LineaLoader lineaLoader;
    @Mock private TotalCalculator totalCalculator;

    @InjectMocks
    private AddProductToCarritoUseCase addProductToCarritoUseCase;

    private Carrito testCarrito() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        return carrito;
    }

    private Product testProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        return product;
    }

    @Test
    void execute_shouldAddProductAndUpdateTotal() {
        Carrito carrito = testCarrito();
        Carrito updatedCarrito = testCarrito();
        updatedCarrito.setTotal(40.0);
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setSubtotal(40.0);
        linea.setQuantity(2);
        updatedCarrito.setLineas(List.of(linea));

        when(getCarritoRepository.findById(1)).thenReturn(Mono.just(carrito), Mono.just(updatedCarrito));
        when(getProductRepository.findById(1)).thenReturn(Mono.just(testProduct()));
        when(createLineaCarritoRepository.save(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(totalCalculator.recalculate(1)).thenReturn(Mono.empty());
        when(lineaLoader.loadLineas(any(Carrito.class))).thenReturn(Mono.just(updatedCarrito));

        StepVerifier.create(addProductToCarritoUseCase.execute(1, 1, 2))
                .expectNextMatches(result -> result.getTotal() == 40.0)
                .verifyComplete();

        verify(createLineaCarritoRepository, times(1)).save(any(LineaCarrito.class));
    }

    @Test
    void execute_ifCarritoNotExists_shouldReturnEmpty() {
        when(getCarritoRepository.findById(999)).thenReturn(Mono.empty());
        when(getProductRepository.findById(1)).thenReturn(Mono.just(testProduct()));

        StepVerifier.create(addProductToCarritoUseCase.execute(999, 1, 2))
                .verifyComplete();

        verify(createLineaCarritoRepository, never()).save(any(LineaCarrito.class));
    }

    @Test
    void execute_ifProductNotExists_shouldReturnEmpty() {
        when(getCarritoRepository.findById(1)).thenReturn(Mono.just(testCarrito()));
        when(getProductRepository.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(addProductToCarritoUseCase.execute(1, 999, 2))
                .verifyComplete();

        verify(createLineaCarritoRepository, never()).save(any(LineaCarrito.class));
    }
}