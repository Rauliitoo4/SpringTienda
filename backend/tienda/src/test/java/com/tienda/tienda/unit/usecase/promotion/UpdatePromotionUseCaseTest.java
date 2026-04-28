package com.tienda.tienda.unit.usecase.promotion;

import com.tienda.tienda.promotion.application.usecase.UpdatePromotionUseCase;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.domain.repository.GetPromotionRepository;
import com.tienda.tienda.promotion.domain.repository.UpdatePromotionRepository;
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
class UpdatePromotionUseCaseTest {

    @Mock private GetPromotionRepository getPromotionRepository;
    @Mock private UpdatePromotionRepository updatePromotionRepository;

    @InjectMocks
    private UpdatePromotionUseCase updatePromotionUseCase;

    private Promotion testPromotion() {
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescription("Rebajas verano");
        promo.setDiscount(20.0);
        return promo;
    }

    @Test
    void execute_shouldUpdateAndReturnPromotion() {
        Promotion promo = testPromotion();
        when(getPromotionRepository.findById(1)).thenReturn(Mono.just(promo));
        when(updatePromotionRepository.save(any(Promotion.class))).thenReturn(Mono.just(promo));

        Promotion changes = new Promotion();
        changes.setDiscount(30.0);

        StepVerifier.create(updatePromotionUseCase.execute(1, changes))
                .expectNextMatches(result -> result.getDiscount() == 30.0)
                .verifyComplete();

        verify(updatePromotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getPromotionRepository.findById(999)).thenReturn(Mono.empty());

        Promotion changes = new Promotion();
        changes.setDiscount(30.0);

        StepVerifier.create(updatePromotionUseCase.execute(999, changes))
                .verifyComplete();
    }
}