package com.tienda.tienda.unit.usecase.promotion;

import com.tienda.tienda.product.application.port.output.ProductPromotionOutputPort;
import com.tienda.tienda.promotion.application.usecase.DeletePromotionUseCase;
import com.tienda.tienda.promotion.application.port.output.DeletePromotionOutputPort;
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

    @Mock private DeletePromotionOutputPort deletePromotionOutputPort;
    @Mock private ProductPromotionOutputPort productPromotionOutputPort;

    @InjectMocks
    private DeletePromotionUseCase deletePromotionUseCase;

    @Test
    void execute_ifExists_shouldReturnTrue() {
        when(deletePromotionOutputPort.existsById(1)).thenReturn(Mono.just(true));
        when(productPromotionOutputPort.deleteByPromotionId(1)).thenReturn(Mono.empty());
        when(deletePromotionOutputPort.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(deletePromotionUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deletePromotionOutputPort, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(deletePromotionOutputPort.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(deletePromotionUseCase.execute(999))
                .expectNext(false)
                .verifyComplete();

        verify(deletePromotionOutputPort, never()).deleteById(anyInt());
    }
}