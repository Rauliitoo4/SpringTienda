package com.tienda.carritoservice.unit.usecase;

import com.tienda.carritoservice.application.port.output.DeleteLineaCarritoOutputPort;
import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.carritoservice.application.service.TotalCalculator;
import com.tienda.carritoservice.application.usecase.DeleteLineaCarritoUseCase;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteLineaCarritoUseCaseTest {

    @Mock private GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    @Mock private DeleteLineaCarritoOutputPort deleteLineaCarritoOutputPort;
    @Mock private TotalCalculator totalCalculator;

    @InjectMocks
    private DeleteLineaCarritoUseCase deleteLineaCarritoUseCase;

    @Test
    void execute_shouldDeleteLineaAndRecalculateTotal() {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setCarritoId(1);

        when(getLineaCarritoOutputPort.findById(1)).thenReturn(Mono.just(linea));
        when(deleteLineaCarritoOutputPort.deleteById(1)).thenReturn(Mono.empty());
        when(totalCalculator.recalculate(1)).thenReturn(Mono.empty());

        StepVerifier.create(deleteLineaCarritoUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deleteLineaCarritoOutputPort, times(1)).deleteById(1);
        verify(totalCalculator, times(1)).recalculate(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(getLineaCarritoOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(deleteLineaCarritoUseCase.execute(99))
                .expectNext(false)
                .verifyComplete();

        verify(deleteLineaCarritoOutputPort, never()).deleteById(anyInt());
        verify(totalCalculator, never()).recalculate(anyInt());
    }
}