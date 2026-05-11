package com.tienda.promotionservice.unit.usecase;

import com.tienda.promotionservice.application.port.output.GetPromotionOutputPort;
import com.tienda.promotionservice.application.port.output.UpdatePromotionOutputPort;
import com.tienda.promotionservice.application.usecase.UpdatePromotionUseCase;
import com.tienda.promotionservice.domain.model.Promotion;
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

    @Mock private UpdatePromotionOutputPort updatePromotionOutputPort;
    @Mock private GetPromotionOutputPort getPromotionOutputPort;

    @InjectMocks
    private UpdatePromotionUseCase updatePromotionUseCase;

    private Promotion testPromotion() {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setDiscount(10.0);
        promotion.setDescription("Descuento verano");
        return promotion;
    }

    @Test
    void execute_shouldUpdateFieldsAndSave() {
        Promotion existing = testPromotion();
        Promotion updated = new Promotion();
        updated.setDiscount(20.0);
        updated.setDescription("Descuento actualizado");

        Promotion saved = new Promotion();
        saved.setId(1);
        saved.setDiscount(20.0);
        saved.setDescription("Descuento actualizado");

        when(getPromotionOutputPort.findById(1)).thenReturn(Mono.just(existing));
        when(updatePromotionOutputPort.save(any(Promotion.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(updatePromotionUseCase.execute(1, updated))
                .expectNextMatches(result ->
                        result.getDiscount() == 20.0 && result.getDescription().equals("Descuento actualizado"))
                .verifyComplete();

        verify(updatePromotionOutputPort, times(1)).save(any(Promotion.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getPromotionOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(updatePromotionUseCase.execute(99, testPromotion()))
                .verifyComplete();

        verify(updatePromotionOutputPort, never()).save(any());
    }
}