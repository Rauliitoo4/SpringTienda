package com.tienda.carritoservice.unit.usecase;

import com.tienda.carritoservice.application.port.output.CreateCarritoOutputPort;
import com.tienda.carritoservice.application.usecase.CreateCarritoUseCase;
import com.tienda.carritoservice.domain.model.Carrito;
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

    @Mock
    private CreateCarritoOutputPort createCarritoOutputPort;

    @InjectMocks
    private CreateCarritoUseCase createCarritoUseCase;

    @Test
    void execute_shouldCreateCarritoWithTotalZero() {
        Carrito saved = new Carrito();
        saved.setId(1);
        saved.setTotal(0.0);

        when(createCarritoOutputPort.save(any(Carrito.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(createCarritoUseCase.execute())
                .expectNextMatches(result -> result.getId() == 1 && result.getTotal() == 0.0)
                .verifyComplete();

        verify(createCarritoOutputPort, times(1)).save(any(Carrito.class));
    }
}