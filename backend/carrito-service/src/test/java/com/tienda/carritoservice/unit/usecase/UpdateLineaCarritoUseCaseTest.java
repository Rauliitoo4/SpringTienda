package com.tienda.carritoservice.unit.usecase;

import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.carritoservice.application.port.output.UpdateLineaCarritoOutputPort;
import com.tienda.carritoservice.application.service.ProductLoader;
import com.tienda.carritoservice.application.service.TotalCalculator;
import com.tienda.carritoservice.application.usecase.UpdateLineaCarritoUseCase;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Product;
import com.tienda.carritoservice.domain.model.Size;
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

    @Mock private GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    @Mock private UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort;
    @Mock private ProductLoader productLoader;
    @Mock private TotalCalculator totalCalculator;

    @InjectMocks
    private UpdateLineaCarritoUseCase updateLineaCarritoUseCase;

    private LineaCarrito testLinea() {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(40.0);
        linea.setSize(Size.M);
        linea.setCarritoId(1);
        linea.setProductId(1);
        return linea;
    }

    private Product testProduct() {
        return new Product(1, "Camiseta", 20.0, 20.0, null, null);
    }

    @Test
    void execute_shouldUpdateQuantityAndRecalculateSubtotal() {
        LineaCarrito linea = testLinea();
        LineaCarrito updated = testLinea();
        updated.setQuantity(5);
        updated.setSubtotal(100.0);

        when(getLineaCarritoOutputPort.findById(1)).thenReturn(Mono.just(linea), Mono.just(updated));
        when(productLoader.loadProduct(1)).thenReturn(Mono.just(testProduct()));
        when(updateLineaCarritoOutputPort.save(any(LineaCarrito.class))).thenReturn(Mono.just(updated));
        when(totalCalculator.recalculate(1)).thenReturn(Mono.empty());

        StepVerifier.create(updateLineaCarritoUseCase.execute(1, 5))
                .expectNextMatches(result -> result.getQuantity() == 5 && result.getSubtotal() == 100.0)
                .verifyComplete();

        verify(updateLineaCarritoOutputPort, times(1)).save(any(LineaCarrito.class));
        verify(totalCalculator, times(1)).recalculate(1);
    }

    @Test
    void execute_ifQuantityIsZero_shouldReturnEmpty() {
        when(getLineaCarritoOutputPort.findById(1)).thenReturn(Mono.just(testLinea()));

        StepVerifier.create(updateLineaCarritoUseCase.execute(1, 0))
                .verifyComplete();

        verify(updateLineaCarritoOutputPort, never()).save(any(LineaCarrito.class));
    }

    @Test
    void execute_ifLineaNotExists_shouldReturnEmpty() {
        when(getLineaCarritoOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(updateLineaCarritoUseCase.execute(99, 5))
                .verifyComplete();

        verify(updateLineaCarritoOutputPort, never()).save(any(LineaCarrito.class));
    }
}