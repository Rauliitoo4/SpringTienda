package com.tienda.promotionservice.unit.usecase;

import com.tienda.promotionservice.application.port.output.GetPromotionOutputPort;
import com.tienda.promotionservice.application.usecase.GetPromotionUseCase;
import com.tienda.promotionservice.domain.model.Promotion;
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

    @Mock
    private GetPromotionOutputPort getPromotionOutputPort;

    @InjectMocks
    private GetPromotionUseCase getPromotionUseCase;

    private Promotion testPromotion() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setDiscount(10.0);
        promotion.setDescription("Descuento verano");
        return promotion;
    }

    @Test
    void execute_shouldReturnPromotion() {
        when(getPromotionOutputPort.findById(1)).thenReturn(Mono.just(testPromotion()));

        StepVerifier.create(getPromotionUseCase.execute(1))
                .expectNextMatches(result -> result.getId() == 1 && result.getDiscount() == 10.0)
                .verifyComplete();
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getPromotionOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(getPromotionUseCase.execute(99))
                .verifyComplete();
    }

    @Test
    void executeAll_shouldReturnAllPromotions() {
        when(getPromotionOutputPort.findAll()).thenReturn(Flux.just(testPromotion(), testPromotion()));

        StepVerifier.create(getPromotionUseCase.executeAll())
                .expectNextCount(2)
                .verifyComplete();
    }
}