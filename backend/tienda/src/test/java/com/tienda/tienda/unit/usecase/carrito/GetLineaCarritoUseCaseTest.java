package com.tienda.tienda.unit.usecase.carrito;

import com.tienda.tienda.carrito.application.service.ProductLoader;
import com.tienda.tienda.carrito.application.usecase.GetLineaCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
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
class GetLineaCarritoUseCaseTest {

    @Mock private GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    @Mock private ProductLoader productLoader;

    @InjectMocks
    private GetLineaCarritoUseCase getLineaCarritoUseCase;

    private LineaCarrito testLinea() {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setQuantity(2);
        linea.setSubtotal(40.0);
        linea.setCarritoId(1);
        linea.setProductId(1);
        return linea;
    }

    @Test
    void execute_shouldReturnLinea() {
        LineaCarrito linea = testLinea();
        when(getLineaCarritoOutputPort.findById(1)).thenReturn(Mono.just(linea));
        when(productLoader.loadProduct(any(LineaCarrito.class))).thenReturn(Mono.just(linea));

        StepVerifier.create(getLineaCarritoUseCase.execute(1))
                .expectNextMatches(result ->
                        result.getId() == 1 &&
                                result.getQuantity() == 2 &&
                                result.getSubtotal() == 40.0)
                .verifyComplete();
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getLineaCarritoOutputPort.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(getLineaCarritoUseCase.execute(999))
                .verifyComplete();
    }

    @Test
    void executeAll_shouldReturnAllLineas() {
        LineaCarrito linea = testLinea();
        when(getLineaCarritoOutputPort.findAll()).thenReturn(Flux.just(linea));
        when(productLoader.loadProduct(any(LineaCarrito.class))).thenReturn(Mono.just(linea));

        StepVerifier.create(getLineaCarritoUseCase.executeAll())
                .expectNextMatches(result -> result.getId() == 1)
                .verifyComplete();
    }
}