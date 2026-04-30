package com.tienda.tienda.unit.usecase.carrito;

import com.tienda.tienda.carrito.application.usecase.CreateCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.application.port.output.CreateCarritoOutputPort;
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
class CreateCarritoUseCaseTest {

    @Mock private CreateCarritoOutputPort createCarritoOutputPort;

    @InjectMocks
    private CreateCarritoUseCase createCarritoUseCase;

    @Test
    void execute_shouldCreateCarritoWithZeroTotal() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        when(createCarritoOutputPort.save(any(Carrito.class))).thenReturn(Mono.just(carrito));

        StepVerifier.create(createCarritoUseCase.execute())
                .expectNextMatches(result ->
                        result.getId() == 1 &&
                                result.getTotal() == 0.0)
                .verifyComplete();

        verify(createCarritoOutputPort, times(1)).save(any(Carrito.class));
    }
}