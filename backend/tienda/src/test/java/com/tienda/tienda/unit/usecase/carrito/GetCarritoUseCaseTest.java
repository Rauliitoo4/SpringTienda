package com.tienda.tienda.unit.usecase.carrito;

import com.tienda.tienda.carrito.application.helper.LineaLoader;
import com.tienda.tienda.carrito.application.usecase.GetCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.repository.GetCarritoRepository;
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
class GetCarritoUseCaseTest {

    @Mock private GetCarritoRepository getCarritoRepository;
    @Mock private LineaLoader lineaLoader;

    @InjectMocks
    private GetCarritoUseCase getCarritoUseCase;

    private Carrito testCarrito() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        return carrito;
    }

    @Test
    void execute_shouldReturnCarritoWithLineas() {
        Carrito carrito = testCarrito();
        when(getCarritoRepository.findById(1)).thenReturn(Mono.just(carrito));
        when(lineaLoader.loadLineas(any(Carrito.class))).thenReturn(Mono.just(carrito));

        StepVerifier.create(getCarritoUseCase.execute(1))
                .expectNextMatches(result ->
                        result.getId() == 1 &&
                                result.getTotal() == 0.0)
                .verifyComplete();
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getCarritoRepository.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(getCarritoUseCase.execute(999))
                .verifyComplete();
    }
}