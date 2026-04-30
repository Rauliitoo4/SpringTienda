package com.tienda.tienda.unit.usecase.promotion;

import com.tienda.tienda.promotion.application.usecase.GetPromotionUseCase;
import com.tienda.tienda.promotion.domain.model.Promotion;
import com.tienda.tienda.promotion.application.port.output.GetPromotionOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPromotionUseCaseTest {

    @Mock private GetPromotionOutputPort getPromotionOutputPort;

    @InjectMocks
    private GetPromotionUseCase getPromotionUseCase;

    private Promotion testPromotion() {
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescription("Rebajas verano");
        promo.setDiscount(20.0);
        return promo;
    }

    @Test
    void execute_shouldReturnPromotion() {
        Promotion promo = testPromotion();
        when(getPromotionOutputPort.findById(1)).thenReturn(Mono.just(promo));

        StepVerifier.create(getPromotionUseCase.execute(1))
                .expectNextMatches(result ->
                        result.getDescription().equals("Rebajas verano") &&
                                result.getDiscount() == 20.0)
                .verifyComplete();
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getPromotionOutputPort.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(getPromotionUseCase.execute(999))
                .verifyComplete();
    }

    @Test
    void executeAll_shouldReturnAllPromotions() {
        Promotion promo = testPromotion();
        when(getPromotionOutputPort.findAll()).thenReturn(Flux.just(promo));

        StepVerifier.create(getPromotionUseCase.executeAll())
                .expectNextMatches(result -> result.getDescription().equals("Rebajas verano"))
                .verifyComplete();
    }
}