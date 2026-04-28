package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.product.application.usecase.DeleteProductUseCase;
import com.tienda.tienda.product.domain.repository.DeleteProductRepository;
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

    @Mock private DeleteProductRepository deleteProductRepository;

    @InjectMocks
    private DeleteProductUseCase deleteProductUseCase;

    @Test
    void execute_ifExists_shouldReturnTrue() {
        when(deleteProductRepository.existsById(1)).thenReturn(Mono.just(true));
        when(deleteProductRepository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(deleteProductUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deleteProductRepository, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(deleteProductRepository.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteProductUseCase.execute(999))
                .expectNext(false)
                .verifyComplete();

        verify(deleteProductRepository, never()).deleteById(anyInt());
    }
}