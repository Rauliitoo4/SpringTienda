package com.tienda.tienda.unit.usecase.carrito;

import com.tienda.tienda.carrito.application.helper.TotalCalculator;
import com.tienda.tienda.carrito.application.usecase.DeleteLineaCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.DeleteLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
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

    private LineaCarrito testLinea() {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(1);
        linea.setCarritoId(1);
        linea.setQuantity(2);
        linea.setSubtotal(40.0);
        return linea;
    }

    @Test
    void execute_ifExists_shouldReturnTrue() {
        LineaCarrito linea = testLinea();
        when(getLineaCarritoOutputPort.findById(1)).thenReturn(Mono.just(linea));
        when(deleteLineaCarritoOutputPort.deleteById(1)).thenReturn(Mono.empty());
        when(totalCalculator.recalculate(1)).thenReturn(Mono.empty());

        StepVerifier.create(deleteLineaCarritoUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deleteLineaCarritoOutputPort, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(getLineaCarritoOutputPort.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(deleteLineaCarritoUseCase.execute(999))
                .expectNext(false)
                .verifyComplete();

        verify(deleteLineaCarritoOutputPort, never()).deleteById(anyInt());
    }
}