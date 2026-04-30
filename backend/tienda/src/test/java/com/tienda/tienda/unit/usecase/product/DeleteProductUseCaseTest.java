package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.product.application.usecase.DeleteProductUseCase;
import com.tienda.tienda.product.application.port.output.DeleteProductOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductUseCaseTest {

    @Mock private DeleteProductOutputPort deleteProductOutputPort;

    @InjectMocks
    private DeleteProductUseCase deleteProductUseCase;

    @Test
    void execute_ifExists_shouldReturnTrue() {
        when(deleteProductOutputPort.existsById(1)).thenReturn(Mono.just(true));
        when(deleteProductOutputPort.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(deleteProductUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deleteProductOutputPort, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(deleteProductOutputPort.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteProductUseCase.execute(999))
                .expectNext(false)
                .verifyComplete();

        verify(deleteProductOutputPort, never()).deleteById(anyInt());
    }
}