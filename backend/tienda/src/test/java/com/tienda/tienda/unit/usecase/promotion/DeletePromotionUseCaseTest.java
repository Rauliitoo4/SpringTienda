package com.tienda.tienda.unit.usecase.promotion;

import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.promotion.application.usecase.DeletePromotionUseCase;
import com.tienda.tienda.promotion.domain.repository.DeletePromotionRepository;
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

    @Mock private DeletePromotionRepository deletePromotionRepository;
    @Mock private ProductPromotionRepository productPromotionRepository;

    @InjectMocks
    private DeletePromotionUseCase deletePromotionUseCase;

    @Test
    void execute_ifExists_shouldReturnTrue() {
        when(deletePromotionRepository.existsById(1)).thenReturn(Mono.just(true));
        when(productPromotionRepository.deleteByPromotionId(1)).thenReturn(Mono.empty());
        when(deletePromotionRepository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(deletePromotionUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deletePromotionRepository, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(deletePromotionRepository.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(deletePromotionUseCase.execute(999))
                .expectNext(false)
                .verifyComplete();

        verify(deletePromotionRepository, never()).deleteById(anyInt());
    }
}