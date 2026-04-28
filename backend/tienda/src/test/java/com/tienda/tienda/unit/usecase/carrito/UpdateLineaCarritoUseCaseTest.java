package com.tienda.tienda.unit.usecase.carrito;

import com.tienda.tienda.carrito.application.helper.ProductLoader;
import com.tienda.tienda.carrito.application.helper.TotalCalculator;
import com.tienda.tienda.carrito.application.usecase.GetLineaCarritoUseCase;
import com.tienda.tienda.carrito.application.usecase.UpdateLineaCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.UpdateLineaCarritoRepository;
import com.tienda.tienda.product.domain.model.Product;
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
class UpdateLineaCarritoUseCaseTest {

    @Mock private GetLineaCarritoRepository getLineaCarritoRepository;
    @Mock private UpdateLineaCarritoRepository updateLineaCarritoRepository;
    @Mock private ProductLoader productLoader;
    @Mock private TotalCalculator totalCalculator;

    @InjectMocks
    private UpdateLineaCarritoUseCase updateLineaCarritoUseCase;

    private LineaCarrito testLinea() {
        Product product = new Product();
        product.setId(1);
        product.setFinalPrice(20.0);

        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(40.0);
        linea.setCarritoId(1);
        linea.setProductId(1);
        linea.setProduct(product);
        return linea;
    }

    @Test
    void execute_shouldUpdateQuantityAndRecalculateSubtotal() {
        LineaCarrito linea = testLinea();
        when(getLineaCarritoRepository.findById(1)).thenReturn(Mono.just(linea));
        when(productLoader.loadProduct(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(updateLineaCarritoRepository.save(any(LineaCarrito.class))).thenReturn(Mono.just(linea));
        when(totalCalculator.recalculate(1)).thenReturn(Mono.empty());

        StepVerifier.create(updateLineaCarritoUseCase.execute(1, 5))
                .expectNextMatches(result ->
                        result.getQuantity() == 5 &&
                                result.getSubtotal() == 100.0)
                .verifyComplete();

        verify(updateLineaCarritoRepository, times(1)).save(any(LineaCarrito.class));
    }

    @Test
    void execute_ifQuantityZero_shouldReturnEmpty() {
        LineaCarrito linea = testLinea();
        when(getLineaCarritoRepository.findById(1)).thenReturn(Mono.just(linea));

        StepVerifier.create(updateLineaCarritoUseCase.execute(1, 0))
                .verifyComplete();

        verify(updateLineaCarritoRepository, never()).save(any(LineaCarrito.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getLineaCarritoRepository.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(updateLineaCarritoUseCase.execute(999, 5))
                .verifyComplete();
    }
}