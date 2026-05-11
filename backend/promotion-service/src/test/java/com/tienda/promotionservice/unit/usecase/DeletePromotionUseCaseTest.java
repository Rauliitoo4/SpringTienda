package com.tienda.promotionservice.unit.usecase;

import com.tienda.promotionservice.application.port.output.DeletePromotionOutputPort;
import com.tienda.promotionservice.application.usecase.DeletePromotionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePromotionUseCaseTest {

    @Mock
    private DeletePromotionOutputPort deletePromotionOutputPort;

    @InjectMocks
    private DeletePromotionUseCase deletePromotionUseCase;

    @Test
    void execute_shouldDeleteAndReturnTrue() {
        when(deletePromotionOutputPort.existsById(1)).thenReturn(Mono.just(true));
        when(deletePromotionOutputPort.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(deletePromotionUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deletePromotionOutputPort, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(deletePromotionOutputPort.existsById(99)).thenReturn(Mono.just(false));

        StepVerifier.create(deletePromotionUseCase.execute(99))
                .expectNext(false)
                .verifyComplete();

        verify(deletePromotionOutputPort, never()).deleteById(anyInt());
    }
}