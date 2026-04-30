package com.tienda.tienda.unit.usecase.promotion;

import com.tienda.tienda.promotion.application.usecase.CreatePromotionUseCase;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.application.port.output.CreatePromotionOutputPort;
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
class CreatePromotionUseCaseTest {

    @Mock private CreatePromotionOutputPort createPromotionOutputPort;

    @InjectMocks
    private CreatePromotionUseCase createPromotionUseCase;

    private Promotion testPromotion() {
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescription("Rebajas verano");
        promo.setDiscount(20.0);
        return promo;
    }

    @Test
    void execute_shouldSaveAndReturnPromotion() {
        Promotion promo = testPromotion();
        when(createPromotionOutputPort.save(any(Promotion.class))).thenReturn(Mono.just(promo));

        StepVerifier.create(createPromotionUseCase.execute(promo))
                .expectNextMatches(result ->
                        result.getDescription().equals("Rebajas verano") &&
                                result.getDiscount() == 20.0)
                .verifyComplete();

        verify(createPromotionOutputPort, times(1)).save(any(Promotion.class));
    }
}