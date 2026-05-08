package com.tienda.carritoservice.unit.usecase;

import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.carritoservice.application.usecase.GetLineaCarritoUseCase;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLineaCarritoUseCaseTest {

    @Mock
    private GetLineaCarritoOutputPort getLineaCarritoOutputPort;

    @InjectMocks
    private GetLineaCarritoUseCase getLineaCarritoUseCase;

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

    @Test
    void execute_shouldReturnLinea() {
        when(getLineaCarritoOutputPort.findById(1)).thenReturn(Mono.just(testLinea()));

        StepVerifier.create(getLineaCarritoUseCase.execute(1))
                .expectNextMatches(result -> result.getId() == 1 && result.getQuantity() == 2)
                .verifyComplete();
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getLineaCarritoOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(getLineaCarritoUseCase.execute(99))
                .verifyComplete();
    }

    @Test
    void executeAll_shouldReturnAllLineas() {
        when(getLineaCarritoOutputPort.findAll()).thenReturn(Flux.just(testLinea(), testLinea()));

        StepVerifier.create(getLineaCarritoUseCase.executeAll())
                .expectNextCount(2)
                .verifyComplete();
    }
}