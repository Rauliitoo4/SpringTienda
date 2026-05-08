package com.tienda.carritoservice.unit.usecase;

import com.tienda.carritoservice.application.port.output.GetCarritoOutputPort;
import com.tienda.carritoservice.application.service.LineaLoader;
import com.tienda.carritoservice.application.usecase.GetCarritoUseCase;
import com.tienda.carritoservice.domain.model.Carrito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCarritoUseCaseTest {

    @Mock private GetCarritoOutputPort getCarritoOutputPort;
    @Mock private LineaLoader lineaLoader;

    @InjectMocks
    private GetCarritoUseCase getCarritoUseCase;

    @Test
    void execute_shouldReturnCarritoWithLineas() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(40.0);
        carrito.setLineas(List.of());

        Carrito carritoWithLineas = new Carrito();
        carritoWithLineas.setId(1);
        carritoWithLineas.setTotal(40.0);

        when(getCarritoOutputPort.findById(1)).thenReturn(Mono.just(carrito));
        when(lineaLoader.loadLineas(carrito)).thenReturn(Mono.just(carritoWithLineas));

        StepVerifier.create(getCarritoUseCase.execute(1))
                .expectNextMatches(result -> result.getId() == 1 && result.getTotal() == 40.0)
                .verifyComplete();

        verify(lineaLoader, times(1)).loadLineas(carrito);
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getCarritoOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(getCarritoUseCase.execute(99))
                .verifyComplete();

        verify(lineaLoader, never()).loadLineas(any());
    }
}