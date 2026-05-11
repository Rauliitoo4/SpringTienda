package com.tienda.promotionservice.unit.usecase;

import com.tienda.promotionservice.application.port.output.CreatePromotionOutputPort;
import com.tienda.promotionservice.application.usecase.CreatePromotionUseCase;
import com.tienda.promotionservice.domain.model.Promotion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePromotionUseCaseTest {

    @Mock
    private CreatePromotionOutputPort createPromotionOutputPort;

    @InjectMocks
    private CreatePromotionUseCase createPromotionUseCase;

    @Test
    void execute_shouldCreateAndReturnPromotion() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setDiscount(10.0);
        promotion.setDescription("Descuento verano");

        when(createPromotionOutputPort.save(promotion)).thenReturn(Mono.just(promotion));

        StepVerifier.create(createPromotionUseCase.execute(promotion))
                .expectNextMatches(result -> result.getId() == 1 && result.getDiscount() == 10.0)
                .verifyComplete();

        verify(createPromotionOutputPort, times(1)).save(promotion);
    }
}